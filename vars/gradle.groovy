/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/
import pipeline.*

def git = new git.GitMethods();
def currentBranch=env.GIT_BRANCH;
def releaseBranchName= 'release-v1-0-0'

def call(String type, String chosenStages, String jobName){
    figlet type
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

def buildAndTest() {
    checkIfBrandUpdated();
    figlet "buildAndTest"
    bat './gradlew clean build'
    println(" Ejecutado")
}

def sonar() {
        figlet "sonar"
    def scannerHome = tool 'sonar';
    withSonarQubeEnv('sonar') {
        bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle2 -Dsonar.java.binaries=build"
    }   
}

def runJar() {
      figlet "runJar"
    bat 'start gradlew bootRun'
    sleep 7
    println(" Ejecutado")
}


def downloadNexus(){
    figlet "downloadNexus"
    bat 'curl -X GET -u admin:Mortal2112 http://localhost:8081/repository/test-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1-develop/DevOpsUsach2020-0.0.1-develop.jar -O'
    sleep 7
}

def runDownload() {
    figlet 'runDownloadedJar'
    bat "start gradlew bootRun &"
    sleep 7
}

def rest() {
    figlet "rest"
    bat "curl -X GET http://localhost:8082/rest/mscovid/test?msg=testing"
    println(" Ejecutado")
}

def checkIfBrandUpdated(){

    git.checkIfBrandUpdated(currentBranch,releaseBranchName);
}

def createRelease(){

    if(git.checkIfBrandExists(releaseBranchName)){
        if(git.checkIfBrandUpdated(currentBranch,releaseBranchName)){
            println('rama'+releaseBranchName+' actualizada con '+currentBranch)
        }else{
            git.deleteBranch(releaseBranchName);
            git.createBranch(releaseBranchName,currentBranch);
        }

    }else{
            git.createBranch(releaseBranchName,currentBranch);
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
        def branchName=GIT_BRANCH.replaceAll("origin/","")

       nexusArtifactUploader(
            nexusVersion: 'nexus3',
            protocol: 'http',
            nexusUrl: 'localhost:8081',
            groupId: 'com.devopsusach2020',
            version: '0.0.1-'+branchName,
            repository: 'test-nexus',
            credentialsId: 'nexus',
            artifacts: [
            [artifactId: 'DevOpsUsach2020',
            classifier: '',
            file: 'C:/Users/luisv/.jenkins/workspace/ci-cd/pipeline-cd/DevOpsUsach2020-0.0.1-develop.jar',
            type: 'jar']
            ]
            )
        println(" Ejecutado")


}
return this;