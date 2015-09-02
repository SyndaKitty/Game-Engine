#version 330 core

uniform sampler2D texture_diffuse;

in vec4 pass_Color;
in vec2 pass_TextureCoord;
in vec3 pass_Normal;

out vec4 out_Color;

void main(void) {
	out_Color = vec4(pass_Normal.x, pass_Normal.y, pass_Normal.z, 1.0);
}