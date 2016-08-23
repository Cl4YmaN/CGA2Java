#version 140

in vec2 position;

out vec2 textureCoords;
out vec2 blurTextureCoords[11];

uniform float targetResolution;
uniform float blurDirection;

void main(void){

	gl_Position = vec4(position, 0.0, 1.0);
	textureCoords = vec2((position.x+1.0)/2.0, (1 - (position.y+1.0)/2.0) * -1);
	
	float pixelSize = 1 / targetResolution;
	
	for(int i = -5; i<=5; i++){
		// 1 = horizontal , 0 = vertical
		if(blurDirection > 0.1){
			blurTextureCoords[i+5] = textureCoords + vec2(pixelSize * i,0.0);
		} else {
			blurTextureCoords[i+5] = textureCoords + vec2(0.0, pixelSize * i);
		}
	}
}