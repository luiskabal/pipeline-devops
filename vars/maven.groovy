/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/
import pipeline.*

def call(String type, String chosenStages, String jobName){

    figlet type   
    branch = jobName
    def utils = new test.UtilMethods()    
    def stages = utils.getValidatedStages('maven',chosenStages, jobName)
    stages.each{
        stage(it){
            try {
                "${it}"()
            }
            catch (e) {
                env.FAIL_MESSAGE = "[${USER_NAME}] [${JOB_NAME}] [${params.CHOICE}]  Ejecución fallida en [${it}]"
                error "Stage ${it} tiene problemas: ${e}"
            }
        }
    }
}

def compile(){
bat './mvnw.cmd clean compile -e'
}
def unitTest(){
   bat './mvnw.cmd clean test -e' 
}
def Jar(){
   bat './mvnw.cmd clean package -e' 
}
def Sonar(){
    def scannerHome = tool 'sonar';
    withSonarQubeEnv('sonar') {
    bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
}

 
}
def nexusCI() {
    nexusArtifactUploader(
            nexusVersion: 'nexus3',
            protocol: 'http',
            nexusUrl: 'http://localhost:8081/',
            groupId: 'com.devopsusach2020',
            version: '0.0.3',
            repository: 'test-nexus',
            credentialsId: 'nexus',
            artifacts: [
            [artifactId: 'DevOpsUsach2020',
            classifier: '',
            file: 'C:/Users/luisv/.jenkins/workspace/ejemplo-gradle-LIBRARY/build/libs/DevOpsUsach2020-0.0.1.jar',
            type: 'jar']
            ]
            )
        println(" Ejecutado")
}

def nexusCD() {

}

return this;