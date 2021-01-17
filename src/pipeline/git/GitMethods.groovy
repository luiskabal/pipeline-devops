package pipeline.git

def call(){
	
}

def checkIfBranchExists(releaseBranchName){
	println('checkIfBranchExists')
	bat 'git pull'
    def output = bat (script:"@git ls-remote --heads origin "+releaseBranchName, returnStdout: true)
	respuesta= !output.isEmpty()?true:false
	return respuesta
}

def checkIfBranchUpdated(currentBranch,releaseBranchName){
	bat 'git checkout '+releaseBranchName
	bat 'git pull'
	def output = bat (script:"@git log origin/"+releaseBranchName+"..origin/"+currentBranch, returnStdout: true)
	respuesta= !output.isEmpty()?true:false
	return respuesta
}

def deleteBranch(releaseBranchName){
	bat '''
	git pull
	git push origin --delete '''+ releaseBranchName
}

def createBranch(releaseBranchName,currentBranch){
	bat '''
	git reset --hard HEAD
	git pull
	git checkout ''' +currentBranch+ '''
	git checkout -b ''' +releaseBranchName+ '''
	git push origin ''' +releaseBranchName
}

def getVersion(){
	bat 'git pull'
	def version = bat(script: "@type version.txt", returnStdout: true).trim()
	return version
}

def gitDiff(){                       
    def version = bat (script:"@type version.txt", returnStdout: true).trim()
    def git = new pipeline.git.GitMethods();
    def currentBranch="origin/release-"+version;
    def targetBranch="origin/main"
    //git.gitDiff(currentBranch,targetBranch)
    println('*************** stage gitDiff')   
}

def gitMergeMaster(){
    println('*************** gitMergeMaster')
    def version = bat (script:"@type version.txt", returnStdout: true).trim()
    bat "git switch main"
    bat "git merge origin/release-"+version
    bat "git push origin main"

}

def gitMergeDevelop(){

    def version = bat (script:"@type version.txt", returnStdout: true).trim()
    bat "git switch develop"
    bat "git merge release-"+version
    bat "git push origin develop"
    println('*************** gitMergeDevelop')
    
}

def gitTagMaster(){

    bat "git switch main"
    bat "git tag v1-0-10"
    bat "git push origin --tags"
    println('*************** gitTagMaster')
    
}
return this;