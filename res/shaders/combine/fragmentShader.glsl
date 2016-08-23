#version 330

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D diffuseTexture;
uniform sampler2D positionTexture;
uniform sampler2D normalTexture;
uniform sampler2D textureCoordinateTexture;
uniform sampler2D depthTexture;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;

uniform vec3 lightPosition;

uniform vec3 lightAmbient;
uniform vec3 lightDiffuse;
uniform vec3 lightSpecular;

uniform vec3 matAmbient;
uniform vec3 matSpecular;
uniform float matShininess;


vec4 shade(vec3 toLightVector, vec3 unitNormal, vec3 unitToCameraVector, vec3 matDiffuse, vec3 lightColAmbient, vec3 lightColDiffuse, vec3 lightColSpecular)
{
	float distance = length(toLightVector);
    vec3 unitLightVector = normalize(toLightVector);
    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    float specularFactor = dot(reflectedLightDirection, unitToCameraVector);
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow (specularFactor, matShininess);
    vec3 finalSpecular = dampedFactor * lightColSpecular;

    float nDotl = dot(unitNormal, unitLightVector);
    float brightness = max(nDotl, 0.2);
    vec3 diffuse = brightness * lightColDiffuse;

	return vec4(lightAmbient * matAmbient  + matDiffuse * diffuse + matSpecular * finalSpecular, 1.0);
}



void main(void){
	vec3 normalizedNormal = normalize(texture(normalTexture,textureCoords).xyz);
	vec4 worldPosition =  texture(positionTexture,textureCoords);
	vec4 realPosition = transformationMatrix * worldPosition;
	vec3 normalizedToCamera = normalize((inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz);
	vec3 toLight = lightPosition - worldPosition.xyz;
	vec3 diffuseColor = texture(diffuseTexture,textureCoords).xyz;
	
	out_Color = shade(toLight,normalizedNormal,normalizedToCamera, diffuseColor, lightDiffuse, lightAmbient, lightSpecular);
	//out_Color = vec4(diffuseColor,1.0);
}