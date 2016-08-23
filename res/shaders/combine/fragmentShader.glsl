#version 330

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D blurStrongFront;
uniform sampler2D blurSoftFront;
uniform sampler2D focalPlane;
uniform sampler2D blurSoftBack;
uniform sampler2D blurStrongBack;

void main(void){
	
	out_Color = vec4(0.0);
	out_Color += vec4(texture(blurStrongFront,textureCoords).xyz,1.0);
	out_Color += vec4(texture(blurSoftFront,textureCoords).xyz,1.0);
	out_Color += vec4(texture(focalPlane,textureCoords).xyz,1.0);
	out_Color += vec4(texture(blurSoftBack,textureCoords).xyz,1.0);
	out_Color += vec4(texture(blurStrongBack,textureCoords).xyz,1.0);
	
	//out_Color = vec4(diffuseColor,1.0);
}