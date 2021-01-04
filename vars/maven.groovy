/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(){
	def requestedStage = params.tareas.split(';').toList();
	stage('Compile'){
		env.TAREA='Compile'
		if(requestedStage.contains(env.Tarea)||params.tareas==''){
			bat './mvnw.cmd clean compile -e'
			println(env.Tarea+" Ejecutado")
		}else{
		println("Error ejecutando: "+ env.Tarea)
		}
		
		
	}
	 stage('Unit Test'){
	 env.TAREA='Unit Test'
	 if(requestedStage.contains(env.Tarea)||params.tareas==''){
		bat './mvnw.cmd clean test -e'
	 }
	 else{
		println("Error ejecutando: "+ env.Tarea)
	}
		
	}

	 stage('Jar'){
	 	 env.TAREA='Jar'
	 	 if(requestedStage.contains(env.Tarea)||params.tareas==''){
			bat './mvnw.cmd clean package -e'
	 	 }else{
		println("Error ejecutando: "+ env.Tarea)
	 	 }
		
	}
	stage('Sonar'){
		env.TAREA='Sonar'
		 if(requestedStage.contains(env.Tarea)||params.tareas==''){
		 		def scannerHome = tool 'sonar';
		withSonarQubeEnv('sonar') {
			bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
		}
		 }else{
		println("Error ejecutando: "+ env.Tarea)
	 	 }
	
	}
	stage('Nexus Upload'){
		env.TAREA='Nexus Upload'
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
			file: 'C:/Users/luisv/.jenkins/workspace/ejemplo-gradle-LIBRARY/build/DevOpsUsach2020-0.0.1.jar',
			type: 'jar']
		]
		)
		 }else{
		println("Error ejecutando: "+ env.Tarea)
	 	 }
	
	}

}

return this;