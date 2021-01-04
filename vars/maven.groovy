/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(){
	def requestedStage = params.tareas.split(';').toList();
	stage('Compile'){
		env.TAREA='Compile'

		try {
				if(requestedStage.contains(env.Tarea)||params.tareas==''){
			bat './mvnw.cmd clean compile -e'
			println(env.Tarea+" Ejecutado")
		}else{
		error("Error ejecutando: "+ env.Tarea)
		}
		}
		catch(Exception e) {
			   catchError {
                sh "exit 1"
            }
		}
		
	}
	 stage('Unit Test'){
	 env.TAREA='Unit Test'
	 try {
	 	 	  if(requestedStage.contains(env.Tarea)||params.tareas==''){
		bat './mvnw.cmd clean test -e'
			println(env.Tarea+" Ejecutado")
	 }
	 else{
		error("Error ejecutando: "+ env.Tarea)
	}
	 	 }
	 	 catch(Exception e) {
	 	 		error("Error ejecutando: "+ env.Tarea+" "+ e )
	 	 }
	
		
	}

	 stage('Jar'){
	 	 env.TAREA='Jar'
	 	 	 try {
	 	 		 if(requestedStage.contains(env.Tarea)||params.tareas==''){
			bat './mvnw.cmd clean package -e'
				println(env.Tarea+" Ejecutado")
	 	 }else{
		error("Error ejecutando: "+ env.Tarea)
	 	 }
	 	 }
	 	 catch(Exception e) {
	 	 		error("Error ejecutando: "+ env.Tarea+" "+ e )
	 	 }
	 
		
	}
	stage('Sonar'){
		env.TAREA='Sonar'
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
	 	 		error("Error ejecutando: "+ env.Tarea+" "+ e )
	 	 }
		
	
	}
	stage('Nexus Upload'){
		env.TAREA='Nexus Upload'
			 try {
	 	 		 if(requestedStage.contains(env.Tarea)||params.tareas==''){
 		println(env.Tarea+" Ejecutado")
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
		 }else{
		error("Error ejecutando: "+ env.Tarea)
	 	 }
	 	 }
	 	 catch(Exception e) {
	 	 		error("Error ejecutando: "+ env.Tarea+" "+ e )
	 	 }
	
	
	}

}

return this;