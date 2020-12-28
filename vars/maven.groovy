/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(){
stage('Compile'){
		env.TAREA='Compile'
		bat './mvnw.cmd clean compile -e'
		
	}
	 stage('Unit Test'){
	 env.TAREA='Unit Test'
		bat './mvnw.cmd clean test -e'
		
	}

	 stage('Jar'){
	 	 env.TAREA='Jar'
		bat './mvnw.cmd clean package -e'
		
	}
	stage('Sonar'){
		env.TAREA='Sonar'
		def scannerHome = tool 'sonar';
		withSonarQubeEnv('sonar') {
			bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
		}
	}
	stage('Nexus Upload'){
		env.TAREA='Nexus Upload'
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
			file: 'C:/Users/luisv/.jenkins/workspace/ejemplo-gradle-LIBRARY/build/DevOpsUsach2020-0.0.1.jar',
			type: 'jar']
		]
		)
	}

}

return this;