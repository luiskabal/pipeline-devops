package pipeline.git


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

return this;