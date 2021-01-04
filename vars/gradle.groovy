/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(){
 	def requestedStage = params.tareas.split(';').toList();

	stage('Build & Test') {
		env.Tarea='Build & Test'

		try {
		if(requestedStage.contains(env.Tarea)||params.tareas==''){
			bat './gradlew clean build'
			println(env.Tarea+" Ejecutado")
		}else{
		error("Error ejecutando: "+ env.Tarea)
		}	
		}
		catch(Exception e) {
			
			error("Error ejecutando: "+ env.Tarea+" "+ e)
		}
		
		}
		
		stage('Sonar'){
		env.Tarea='Sonar'
		try {
		if(requestedStage.contains(env.Tarea)||params.tareas==''){
		def scannerHome = tool 'sonar';
			withSonarQubeEnv('sonar') {
				bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
					println(env.Tarea+" Ejecutado")
			}	
		}else{
		error("Error ejecutando: "+ env.Tarea)
		}	
		}
		catch(Exception e) {
				error("Error ejecutando: "+ env.Tarea+" "+ e)
		}
	
		
			
		}
		stage('Run'){
			env.Tarea='Run'

				try {
			
			if(requestedStage.contains(env.Tarea)||params.tareas==''){
				
				bat 'start gradlew bootRun'
				sleep 7
				println(env.Tarea+" Ejecutado")
			}else{
				error("Error ejecutando: "+ env.Tarea)
			}
		}
		catch(Exception e) {
				error("Error ejecutando: "+ env.Tarea+" "+ e)
		}
		
		}
		stage('Test'){
			env.Tarea='Test'

				try {
			if(requestedStage.contains(env.Tarea)||params.tareas==''){
				env.Tarea='Test'
				bat "curl -X GET http://localhost:8082/rest/mscovid/test?msg=testing"
					println(env.Tarea+" Ejecutado")
			}
			else{
				error("Error ejecutando: "+ env.Tarea)
			}
		}
		catch(Exception e) {
				   catchError {
	                sh "exit 1"
	            }
			}
			
	
		}
		stage('Nexus Upload'){
		env.Tarea='Nexus Upload'

			try {
				if(requestedStage.contains(env.Tarea)||params.tareas==''){
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
				println(env.Tarea+" Ejecutado")
			}else{
				error("Error ejecutando: "+ env.Tarea)
			}
		}
		catch(Exception e) {
				error("Error ejecutando: "+ env.Tarea+" "+ e)
		}
	
						
		}	
	
}

return this;