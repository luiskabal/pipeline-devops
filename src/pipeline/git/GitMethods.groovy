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
	bat 'git switch '+currentBranch
	bat 'git pull'	
	bat 'git switch '+releaseBranchName
	bat 'git pull'
	def output = bat (script:"@git log "+releaseBranchName+".."+currentBranch, returnStdout: true)
	respuesta= !output.isEmpty()?true:false
	return respuesta
}

def deleteBranch(releaseBranchName){
	bat '''
	git pull
	git push origin --delete '''+ releaseBranchName
}

def getVersion(){
	bat 'git pull'
	println('getVersion');
	def version = bat(script: "@type version.txt", returnStdout: true).trim()
	return version
}

def gitDiff(){ 
	println('*************** stage gitDiff')                         
    def version = bat (script:"@type version.txt", returnStdout: true).trim()
    def git = new pipeline.git.GitMethods();
    def currentBranch="origin/release-"+version;
    def targetBranch="origin/main"
    def output = bat (script: '@git diff '+currentBranch+'..'+targetBranch+'' , returnStdout:true).trim()
    println('*************** stage gitDiff '+ output) 
   	return output.isEmpty()
}

def gitMergeMaster(){
    println('*************** gitMergeMaster')
    def version = bat (script:"@type version.txt", returnStdout: true).trim()
    bat "git switch main"
    bat "git pull"
    bat "git add --ignore-errors ."
    bat "git stash"
    bat "git merge origin/release-"+version
    bat "git push origin main"

}

def gitMergeDevelop(){

    def version = bat (script:"@type version.txt", returnStdout: true).trim()
    bat "git switch develop"
    bat "git merge origin/release-"+version
    bat "git push origin develop"
    println('*************** gitMergeDevelop')
    
}

def gitTagMaster(){
	def version = bat (script:"@type version.txt", returnStdout: true).trim()
    bat "git switch main"
    bat "git tag "+version
    bat "git push origin --tags"
    println('*************** gitTagMaster')
    
}

def createBranch(releaseBranchName,currentBranch){
	bat '''
	git reset --hard HEAD
	git checkout .
	git pull
	git checkout ''' +currentBranch+ '''
	git checkout -b ''' +releaseBranchName+ '''
	git push origin ''' +releaseBranchName
}


return this;