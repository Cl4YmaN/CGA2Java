#version 330

in vec2 textureCoords;
in vec2 blurTextureCoords[11];

out vec4 out_Color;

uniform sampler2D frameData;



void main(void){
	//GAUSIAN KERNEL FROM http://dev.theomader.com/gaussian-kernel-calculator/
	out_Color = vec4(0.0);
	out_Color += texture(frameData,blurTextureCoords[0]) * 0.000003;
	out_Color += texture(frameData,blurTextureCoords[1])* 0.000229;
	out_Color += texture(frameData,blurTextureCoords[2])* 0.005977;
	out_Color += texture(frameData,blurTextureCoords[3])* 0.060598;
	out_Color += texture(frameData,blurTextureCoords[4])* 0.24173;
	out_Color += texture(frameData,blurTextureCoords[5])* 0.382925;
	out_Color += texture(frameData,blurTextureCoords[6])* 0.24173;
	out_Color += texture(frameData,blurTextureCoords[7])* 0.060598;
	out_Color += texture(frameData,blurTextureCoords[8])* 0.005977;
	out_Color += texture(frameData,blurTextureCoords[9])* 0.000229;
	out_Color += texture(frameData,blurTextureCoords[10])* 0.000003;
}