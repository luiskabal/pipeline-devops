/*
    forma de invocación de método call:
    def ejecucion = load 'script.groovy'
    ejecucion.call()
*/
import pipeline.*

def call(String type, String chosenStages, String jobName){
    figlet type
    def utils = new test.UtilMethods()    
    def stages = utils.getValidatedStages(type,chosenStages, jobName)
    stages.each{
        stage(it){
            try {
                //Validar tipo de app
                utils.validateKindApp(env.GIT_URL)
                //Validar archivos según herramienta
                utils.validateFiles(params.CHOICE)
                //Validar nombre rama release
                def version = readFile "version.txt"
                def releaseBranchName = "release-${version}"
                utils.validateReleaseBranchName(releaseBranchName)

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
    figlet "buildAndTest"
    //bat './gradlew clean build'
    sh './gradlew clean build'
    println(" Ejecutado buildAndTest")
}

def sonar() {
        figlet "sonar"
    def scannerHome = tool 'sonar';
    withSonarQubeEnv('sonar') {
        //bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle2 -Dsonar.java.binaries=build"
        sh "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
    }
    println(" Ejecutado sonar")

}

def runJar() {
      figlet "runJar"
    //bat 'start gradlew bootRun'
    sh 'start gradlew bootRun'
    sleep 15
    println(" Ejecutado runJar")
}


def downloadNexus(){
    figlet "downloadNexus"
    //bat 'curl -X GET -u admin:Mortal2112 http://localhost:8081/repository/test-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1-develop/DevOpsUsach2020-0.0.1-develop.jar -O'
    sh 'curl -X GET -u admin:Mortal2112 http://localhost:8081/repository/test-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1-develop/DevOpsUsach2020-0.0.1-develop.jar -O'
    println(" Ejecutado downloadNexus")    
    sleep 10
}

def runDownload() {
    figlet 'runDownloadedJar'
    //bat "start gradlew bootRun &"
    sh "start gradlew bootRun &"
    println(" Ejecutado runDownload")    
    sleep 7
}

def rest() {
    figlet "rest"
    //bat "curl -X GET http://localhost:8082/rest/mscovid/test?msg=testing"
    "curl -X GET http://localhost:8082/rest/mscovid/test?msg=testing"
    println(" Ejecutado rest")
}

def createRelease(){
    def git = new pipeline.git.GitMethods();
    def currentBranch=env.GIT_BRANCH;
    def version = readFile "version.txt"
    def releaseBranchName= "release-${version}"
    if(git.checkIfBranchExists(releaseBranchName)){
        if(git.checkIfBranchUpdated(currentBranch,releaseBranchName)){
            println('rama'+releaseBranchName+' actualizada con '+currentBranch)
        }else{
            git.deleteBranch(releaseBranchName);
            git.createBranch(releaseBranchName,currentBranch);
        }

    }else{
            git.createBranch(releaseBranchName,currentBranch);
    }
    println(" Ejecutado createRelease")
}

def nexusCI() {
    figlet "nexusCI"
    def jobName = JOB_NAME.replaceAll("/","_")
    def branch = GIT_BRANCH;

   nexusArtifactUploader(
        nexusVersion: 'nexus3',
        protocol: 'http',
        nexusUrl: 'localhost:8081',
        groupId: 'com.devopsusach2020',
        version: '0.0.1-'+branch,
        repository: 'test-nexus',
        credentialsId: 'nexus',
        artifacts: [
        [artifactId: 'DevOpsUsach2020',
        classifier: '',
        file: 'C:/Users/luisv/.jenkins/workspace/'+jobName+'/build/libs/DevOpsUsach2020-0.0.1.jar',
        type: 'jar']
        ]
        )
   println(" Ejecutado nexusCI")
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
    println(" Ejecutado nexusCD")
}

def gitDiff() {
    def git = new pipeline.git.GitMethods();
    def releaseBranchName= 'release-v1-0-0'
    git.gitDiff(releaseBranchName,'main')
}
return this;