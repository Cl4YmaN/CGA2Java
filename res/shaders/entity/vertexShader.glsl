#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 normal;
layout(location = 2) in vec2 textureCoordinates;
layout(location = 3) in vec3 emission;
layout(location = 4) in vec3 ambient;
layout(location = 5) in vec3 specular;
layout(location = 6) in float shininess;

out vec4 worldPosition;
out vec3 worldNormal;
out vec2 textureCoords;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

uniform vec3 lightPos;

void main(void){
    vec4 worldPos = modelMatrix * vec4(position, 1.0);
	gl_Position = projectionMatrix * viewMatrix  * worldPos;    
	//Daten an Fragmentshader weitergeben
	worldPosition = worldPos;
	worldNormal = (transpose(inverse(modelMatrix)) * vec4(normal,0.0)).xyz;
	textureCoords = textureCoordinates;    
}