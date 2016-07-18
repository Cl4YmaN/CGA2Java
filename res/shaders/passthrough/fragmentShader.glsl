#version 140

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D quadTexture;

void main(void){
	out_Color = vec4(texture(quadTexture,textureCoords).xyz,1.0);
	//out_Color = vec4(textureCoords,0.0,1.0);
}