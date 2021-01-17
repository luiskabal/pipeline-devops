package pipeline.git

def call(){
	
}
def checkIfBranchExists(releaseBranchName){
	println('checkIfBranchExists')
	bat 'git pull'
    def output = bat (script:"@git ls-remote origin --heads "+releaseBranchName, returnStdout: true)
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
return this;