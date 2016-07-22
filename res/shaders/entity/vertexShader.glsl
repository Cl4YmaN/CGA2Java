#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 normal;
layout(location = 2) in vec2 textureCoordinates;

out vec4 worldPosition;
out vec3 worldNormal;
out vec2 textureCoords;

uniform mat4 modelMatrix;
uniform mat4 pvmMatrix;


void main(void){
    vec4 worldPos = modelMatrix * vec4(position, 1.0);
	gl_Position = pvmMatrix * vec4(position, 1.0);    
	//Daten an Fragmentshader weitergeben
	worldPosition = worldPos;
	worldNormal = (transpose(inverse(modelMatrix)) * vec4(normal,0.0)).xyz;
	textureCoords = textureCoordinates;
}