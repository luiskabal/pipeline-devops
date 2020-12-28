def call(){

pipeline{
	agent any
	parameters{choice(name: 'buildtool', choices:['gradle','maven'], description:'Parametros de herramientas a elegir')}
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