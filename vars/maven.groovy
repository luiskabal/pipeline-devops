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
    def stages = utils.getValidatedStages(type,chosenStages, jobName)
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
     figlet "compile"
bat './mvnw.cmd clean compile -e'
}
def unitTest(){
    figlet "unitTest"
   bat './mvnw.cmd clean test -e' 
}
def Jar(){
        figlet "Jar"
   bat './mvnw.cmd clean package -e' 
}
def Sonar(){
        figlet "Sonar"
    def scannerHome = tool 'sonar';
    withSonarQubeEnv('sonar') {
    bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
}

 
}
def nexusCI() {
        figlet "nexusCI"
    def jobName=JOB_NAME.replaceAll("/","_")

       nexusArtifactUploader(
            nexusVersion: 'nexus3',
            protocol: 'http',
            nexusUrl: 'localhost:8081',
            groupId: 'com.devopsusach2020',
            version: '0.0.1-'+GIT_BRANCH,
            repository: 'test-nexus',
            credentialsId: 'nexus',
            artifacts: [
            [artifactId: 'DevOpsUsach2020',
            classifier: '',
            file: 'C:/Users/luisv/.jenkins/workspace/'+jobName+'/build/libs/DevOpsUsach2020-0.0.1.jar',
            type: 'jar']
            ]
            )
        println(" Ejecutado")
}

    def nexusCD() {
      figlet "nexusCD"
    def jobName=JOB_NAME.replaceAll("/","_")

       nexusArtifactUploader(
            nexusVersion: 'nexus3',
            protocol: 'http',
            nexusUrl: 'localhost:8081',
            groupId: 'com.devopsusach2020',
            version: '0.0.1-'+GIT_BRANCH,
            repository: 'test-nexus',
            credentialsId: 'nexus',
            artifacts: [
            [artifactId: 'DevOpsUsach2020',
            classifier: '',
            file: 'C:/Users/luisv/.jenkins/workspace/ci-cd/pipeline-cd/DevOpsUsach2020-0.0.1.jar',
            type: 'jar']
            ]
            )
        println(" Ejecutado")
    

}

return this;