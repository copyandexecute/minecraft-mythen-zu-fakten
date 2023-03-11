#version 150
//Credits to Mojang and https://www.youtube.com/watch?v=zgGl-xLlOcs

uniform sampler2D DiffuseSampler;

#define Hue 0.0
#define Contrast 0.5 // [0 0.1 0.2 0.3 0.4 0.5 0.6 0.7 0.8 0.9 1]
#define Saturation 1.0

#define Pr .299
#define Pg .587
#define Pb .114

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;

uniform vec3 Gray;
uniform vec3 RedMatrix;
uniform vec3 GreenMatrix;
uniform vec3 BlueMatrix;
uniform vec3 Offset;
uniform vec3 ColorScale;
uniform float Brightness;

out vec4 fragColor;

void applyHue(inout vec4 color, float hue) {
    float angle = radians(hue);
    vec3 k = vec3(0.57735, 0.57735, 0.57735);
    float cosAngle = cos(angle);
    //Rodrigues' rotation formula
    color.rgb = color.rgb * cosAngle + cross(k, color.rgb) * sin(angle) + k * dot(k, color.rgb) * (1.0 - cosAngle);
}

void applyContrast(inout vec4 color, float contrast) {
    color.rgb = (color.rgb - 0.5) * contrast + 0.5;
}

void applySaturation(inout vec4 color, float saturation) {
    float p = sqrt(color.r * color.r * Pr + color.g * color.g * Pg + color.b * color.b * Pb);
    color.r = p + (color.r - p) * saturation;
    color.g = p + (color.g - p) * saturation;
    color.b = p + (color.b - p) * saturation;
}

void applyBrightness(inout vec4 color, float brightness) {
    color.rgb = color.rgb + vec3(brightness, brightness, brightness);
}

vec4 applyHSBCEffect(vec4 startColor, vec4 hsbc) {
    float h = 360 * hsbc.r;
    float s = hsbc.g * 2;
    float b = hsbc.b * 2 - 1;
    float c = hsbc.a * 2;

    vec4 outputColor = startColor;
    applyHue(outputColor, h);
    applySaturation(outputColor, s);
    applyBrightness(outputColor, b);
    applyContrast(outputColor, c);

    return outputColor;
}

void main() {
    vec4 InTexel = texture(DiffuseSampler, texCoord);

    // Color Matrix
    float RedValue = dot(InTexel.rgb, RedMatrix);
    float GreenValue = dot(InTexel.rgb, GreenMatrix);
    float BlueValue = dot(InTexel.rgb, BlueMatrix);
    vec3 OutColor = vec3(RedValue, GreenValue, BlueValue);

    // Offset & Scale
    OutColor = (OutColor * ColorScale) + Offset;

    // Saturation
    float Luma = dot(OutColor, Gray);
    vec3 Chroma = OutColor - Luma;
    OutColor = (Chroma * Saturation) + Luma;

    fragColor = applyHSBCEffect(vec4(OutColor, 1), vec4(Hue, 0.5, Brightness, Contrast));
}
