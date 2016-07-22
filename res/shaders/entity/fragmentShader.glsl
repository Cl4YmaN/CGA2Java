#version 330 core

in vec4 worldPosition;
in vec3 worldNormal;
in vec2 textureCoords;
 
out vec4 colorDiffuse;
out vec4 positionWorld;
out vec4 normalWorld;

uniform sampler2D primaryTexture;
uniform sampler2D secondaryTexture;
uniform sampler2D blendMap;

uniform float useMultiTexture;

void main(void){

	float blendFactor = texture(blendMap,textureCoords).r;
    colorDiffuse = texture(primaryTexture,textureCoords) * (1-blendFactor) + texture(secondaryTexture,textureCoords) * blendFactor;
	positionWorld = worldPosition;
	normalWorld = vec4(worldNormal,1.0);
}