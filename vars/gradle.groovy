/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(){
  
	stage('Build & Test'){
		env.Tarea='Build & Test'
		bat './gradlew clean build'
		
		}
		
	stage('Sonar'){
		env.Tarea='Sonar'
					def scannerHome = tool 'sonar';
					withSonarQubeEnv('sonar') {
						bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
					}
		}
	stage('Run'){
			env.Tarea='Run'
			bat 'start gradlew bootRun'
			sleep 7
		}
	stage('Test'){
		env.Tarea='Test'
					bat "curl -X GET http://localhost:8082/rest/mscovid/test?msg=testing"
		}
	stage('Nexus Upload'){
		env.Tarea='Nexus Upload'
						nexusArtifactUploader(
						nexusVersion: 'nexus3',
						protocol: 'http',
						nexusUrl: 'http://localhost:8081/',
						groupId: 'com.devopsusach2020',
						version: '0.0.1',
						repository: 'test-nexus',
						credentialsId: 'nexus',
						artifacts: [
						[artifactId: 'DevOpsUsach2020',
						classifier: '',
						file: 'C:/Users/luisv/.jenkins/workspace/ejemplo-gradle-LIBRARY/build/libs/DevOpsUsach2020-0.0.1.jar',
						type: 'jar']
						]
						)
		}
}

return this;