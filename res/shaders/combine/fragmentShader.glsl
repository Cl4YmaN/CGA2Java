#version 330

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D blurStrong;
uniform sampler2D blurSoft;
uniform sampler2D focalPlane;
uniform sampler2D blurTextureSoft;
uniform sampler2D blurTextureStrong;

void main(void){
	
	vec4 colorBlurStrong = vec4(texture(blurStrong,textureCoords).xyz,1.0);
	vec4 colorBlurSoft = vec4(texture(blurSoft,textureCoords).xyz,1.0);
	vec4 colorFocalPlane = vec4(texture(focalPlane,textureCoords).xyz,1.0);
	vec4 colorBlurTextureSoft = vec4(texture(blurTextureSoft,textureCoords).xyz,1.0);
	vec4 colorBlurTextureStrong = vec4(texture(blurTextureStrong,textureCoords).xyz,1.0);
	
	if(colorBlurSoft.x == 1.0){
		out_Color = colorBlurTextureSoft;
	} else if(colorBlurStrong.x == 1.0){
		out_Color = colorBlurTextureStrong;
	} else {
		out_Color = colorFocalPlane;
	}
	
	//out_Color = vec4(0.0);
	//out_Color += vec4(texture(blurStrongFront,textureCoords).xyz,1.0);
	//out_Color += vec4(texture(blurSoftFront,textureCoords).xyz,1.0);
	//out_Color += vec4(texture(focalPlane,textureCoords).xyz,1.0);
	//out_Color += vec4(texture(blurSoftBack,textureCoords).xyz,1.0);
	//out_Color += vec4(texture(blurStrongBack,textureCoords).xyz,1.0);
	
	//out_Color = vec4(blurSoftBack,1.0);
	
	
	//out_Color = vec4(texture(blurTextureStrong,textureCoords).xyz,1.0);
}