#version 330 core

uniform sampler2D texture_diffuse;

in vec2 pass_TextureCoord;
in vec3 pass_Normal;

out vec4 out_Color;

void main(void) {
	out_Color = vec4(abs(int(pass_Normal.x)), abs(int(pass_Normal.y)), abs(int(pass_Normal.z)), 1.0);
	out_Color = texture(texture_diffuse, pass_TextureCoord);
}