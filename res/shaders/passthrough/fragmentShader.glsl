#version 330

in vec2 textureCoords;

out vec4 blurStrong;
out vec4 blurSoft;
out vec4 focalPlane;

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

uniform float limitBlurStrongFront;
uniform float limitBlurSoftFront;
uniform float limitFocalPlane;
uniform float limitBlurSoftBack;
uniform float limitBlurStrongBack;

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

	vec4 temp = shade(toLight,normalizedNormal,normalizedToCamera, diffuseColor, lightDiffuse, lightAmbient, lightSpecular);
	
	float zNear = 0.5;    // TODO: Replace by the zNear of your perspective projection
    float zFar  = 2000.0; // TODO: Replace by the zFar  of your perspective projection
    float depth = texture(depthTexture,textureCoords).x;
    float linearDepth = (2.0 * zNear) / (zFar + zNear - depth * (zFar - zNear)); 
    
	//GET BLENDMASKS FOR BLURED IMAGES
	if(linearDepth <= limitBlurStrongFront || linearDepth > limitBlurSoftBack){
			blurStrong = vec4(1.0);
			blurSoft = vec4(0.0);
	//SoftFront
	} else if((linearDepth > limitBlurStrongFront && linearDepth <= limitBlurSoftFront ) || (linearDepth > limitFocalPlane && linearDepth <= limitBlurSoftBack )) {
			blurStrong = vec4(0.0);
			blurSoft = vec4(1.0);
	}
	
	focalPlane = temp;
	
}