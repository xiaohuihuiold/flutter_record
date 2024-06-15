#extension GL_OES_EGL_image_external : require

precision mediump float;

varying vec4 vTextureCoord;
uniform samplerExternalOES sTexture;
uniform float frameCount;

void main() {
    vec4 color = texture2D(sTexture, vTextureCoord.xy);
    gl_FragColor = color;
}