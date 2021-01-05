def call(){

pipeline{
	agent any
	parameters{
		choice(name: 'buildtool', choices:['gradle','maven'], description:'Parametros de herramientas a elegir')
		text(name: 'tareas', defaultValue: '', description: 'parametros de stages a elegir Gradle:Build & Test;Sonar;Run;Test;Nexus Upload - Maven: Compile;Unit Test;Jar;Sonar;Nexus Upload, separados por ";"')
	}

	stages{
			stage('Pipeline'){
				steps{
					script{
					figlet env.BRANCH_NAME
					figlet params.buildtool	
					figlet BRANCH_NAME
					bat 'set'
					if(params.buildtool == 'gradle' ){
					gradle.call();
					}else{
					maven.call();
					}
					}
				}
				
			}
	}

}


	
}
return this;
