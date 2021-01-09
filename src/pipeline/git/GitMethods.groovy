package pipeline.git

def call(){
	
}
def checkIfBranchExists(releaseBranchName){
	println checkIfBranchExists
	bat 'git pull'
    def output = bat (script:"git ls-remote --heads origin "+releaseBranchName, returnStdout: true)
	respuesta= output.isEmpty()?true:false
	return respuesta
}
def checkIfBranchUpdated(currentBranch,releaseBranchName){
	println checkIfBranchUpdated
	bat 'git pull'
	def output = bat (script:"git log origin/"+releaseBranchName+"..origin/"+currentBranch, returnStdout: true)
	respuesta= output.isEmpty()?true:false

	return respuesta ;
}
def deleteBranch(releaseBranchName){
	println deleteBranch
	bat '''
	git pull
	git push origin --delete '''+ releaseBranchName
}
def createBranch(releaseBranchName,currentBranch){
	println createBranch
	bat 
	'''
	'git reset --hard HEAD'
	'git pull
	'git checkout ''' +currentBranch+ '''
	'git checkout -b ''' +releaseBranchName+ '''
	'git push origin ''' +releaseBranchName
}
return this;