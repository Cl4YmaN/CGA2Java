#version 330 core

in vec4 worldPosition;
in vec3 worldNormal;
in vec2 textureCoords;

out vec4 colorDiffuse;
out vec4 positionWorld;
out vec4 normalWorld;
out vec4 textureCoordst;

uniform sampler2D texture1;

void main(void){

    colorDiffuse = texture(texture1,textureCoords);
	positionWorld = worldPosition;
	normalWorld = vec4(worldNormal,1.0);
	textureCoordst = vec4(textureCoords,0.0,1.0);
}