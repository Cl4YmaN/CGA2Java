#version 330

in vec2 textureCoords;
in vec2 blurTextureCoords[11];

out vec4 blurStrongFront;
out vec4 blurSoftFront;
out vec4 focalPlane;
out vec4 blurSoftBack;
out vec4 blurStrongBack;

uniform sampler2D frameData;
uniform float renderTarget;



void main(void){
	//GAUSIAN KERNEL FROM http://dev.theomader.com/gaussian-kernel-calculator/
	vec4 originColor = texture(frameData,blurTextureCoords[5]);
	if(originColor.a > 0){
		vec4 color = vec4(0.0);
		color += texture(frameData,blurTextureCoords[0]) * 0.000003;
		color += texture(frameData,blurTextureCoords[1])* 0.000229;
		color += texture(frameData,blurTextureCoords[2])* 0.005977;
		color += texture(frameData,blurTextureCoords[3])* 0.060598;
		color += texture(frameData,blurTextureCoords[4])* 0.24173;
		color += texture(frameData,blurTextureCoords[5])* 0.382925;
		color += texture(frameData,blurTextureCoords[6])* 0.24173;
		color += texture(frameData,blurTextureCoords[7])* 0.060598;
		color += texture(frameData,blurTextureCoords[8])* 0.005977;
		color += texture(frameData,blurTextureCoords[9])* 0.000229;
		color += texture(frameData,blurTextureCoords[10])* 0.000003;
		
		if(renderTarget == 0.0) {
			blurStrongFront = color;
		} else if(renderTarget == 1.0) {
			blurSoftFront = color;
		} else if(renderTarget == 2.0) {
			focalPlane = color;
		} else if(renderTarget == 3.0) {
			blurSoftBack = color;
		} else if(renderTarget == 4.0) {
			blurStrongBack = color;
		}
	} else {
		discard;
	}
	
}