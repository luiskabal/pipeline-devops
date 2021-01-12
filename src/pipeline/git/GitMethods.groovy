package pipeline.git

def call(){
	
}
def checkIfBranchExists(releaseBranchName){
	/*bat 'git pull'
    def output = bat (script:"@git ls-remote --heads "+releaseBranchName, returnStdout: true)
	respuesta= !output.isEmpty()?true:false
	*/
	respuesta = sh "git pull; git ls-remote --heads origin ${releaseBranchName} | wc -l"
	return respuesta
}
def checkIfBranchUpdated(currentBranch,releaseBranchName){
	/*bat 'git checkout '+releaseBranchName
	bat 'git pull'
	def output = bat (script:"@git log origin/"+releaseBranchName+"..origin/"+currentBranch, returnStdout: true)
	respuesta= !output.isEmpty()?true:false*/
	respuesta = sh "git pull; git checkout ${releaseBranchName}; git pull ${releaseBranchName} | wc -l"
	return respuesta
}
def deleteBranch(releaseBranchName){
	/*bat '''
	git pull
	git push origin --delete '''+ releaseBranchName*/
	sh "git pull; git push origin --delete ${releaseBranchName}"
}
def createBranch(releaseBranchName,currentBranch){
	/*bat '''
	git reset --hard HEAD
	git pull
	git checkout ''' +currentBranch+ '''
	git checkout -b ''' +releaseBranchName+ '''
	git push origin ''' +releaseBranchName*/

	sh "git reset --hard HEAD; git pull; git checkout ${currentBranch}; git checkout -b ${releaseBranchName}; git push origin ${releaseBranchName}"
}
def getVersion(){
	/*
	bat 'git pull'
	def version = bat(script: "@type version.txt", returnStdout: true).trim()*/
	version = sh "cat version.txt"
	return version
}
def gitDiff(branch1,branch2){
	sh "git pull; git diff ${branch1}..${branch2}"
}
return this;