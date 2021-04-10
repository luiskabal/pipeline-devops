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
    env.NombreStage='buildAndTest'
    figlet "buildAndTest"
    bat './gradlew clean build'
    println(" Ejecutado buildAndTest"+ env.NombreStage)
}
/*def sonar() {
    figlet "sonar"
    def currentBranch=env.GIT_BRANCH;
    env.NombreStage="sonar"
    def scannerHome = tool 'sonar';
    withSonarQubeEnv('sonar') {
        bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey="+currentBranch+" -Dsonar.java.binaries=build"
    }
    println(" Ejecutado sonar")

}

def runJar() {
    figlet "runJar"
     env.NombreStage="runJar"
    bat 'start gradlew bootRun'
    sleep 15
    println(" Ejecutado runJar")
}


def downloadNexus(){
    figlet "downloadNexus"
    env.NombreStage="downloadNexus"
    bat 'curl -X GET -u admin:Mortal2112 http://localhost:8081/repository/test-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1-develop/DevOpsUsach2020-0.0.1-develop.jar -O'
    println(" Ejecutado downloadNexus")    
    sleep 10
}

def runDownload() {
    figlet 'runDownloadedJar'
    env.NombreStage="runDownloadedJar"
    bat "start gradlew bootRun &"
    println(" Ejecutado runDownload")    
    sleep 7
}

def rest() {
    figlet "rest"
    env.NombreStage="rest"
    bat "curl -X GET http://localhost:8082/rest/mscovid/test?msg=testing"
    println(" Ejecutado rest")
}

def createRelease(){
    env.NombreStage="createRelease"

    def git = new pipeline.git.GitMethods();
    def currentBranch=env.GIT_BRANCH;
    def releaseBranchName= 'release-'+git.getVersion()
    println(" EJECUTANDO: createRelease "+releaseBranchName)
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

    def gitDiff(){
        env.NombreStage="gitDiff"
        def git = new pipeline.git.GitMethods();
        git.gitDiff();
    }

    def gitMergeMaster(){
        env.NombreStage="gitMergeMaster"
        def git = new pipeline.git.GitMethods();
        git.gitMergeMaster();

    }

    def gitMergeDevelop(){
        env.NombreStage="gitMergeDevelop"    
        def git = new pipeline.git.GitMethods();
        git.gitMergeDevelop();

    }

    def gitTagMaster(){
        env.NombreStage="gitTagMaster"    

        def git = new pipeline.git.GitMethods();
        git.gitTagMaster();

    }


def nexusCI() {
    figlet "nexusCI"
    env.NombreStage="nexusCI"    

    def jobName = JOB_NAME.replaceAll("/","_")
    def branch = GIT_BRANCH
    def workspace = WORKSPACE
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
        file: workspace+'/build/libs/DevOpsUsach2020-0.0.1.jar',
        type: 'jar']
        ]
        )
   println(" Ejecutado nexusCI")
}

    def nexusCD() {
    figlet "nexusCD"
    env.NombreStage="nexusCD"
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
    }*/

return this;