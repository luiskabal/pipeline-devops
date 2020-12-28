def call(){

pipeline{
	agent any
	parameters{
		choice(name: 'buildtool', choices:['gradle','maven'], description:'Parametros de herramientas a elegir')
		text(name: 'tareas', defaultValue: '', description: 'parametros de stages a elegir, separados por ';'')

	}

	stages{
			stage('Pipeline'){
				steps{
					script{
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