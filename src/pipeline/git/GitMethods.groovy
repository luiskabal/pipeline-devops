package pipeline.git

def call(){
	
}
def checkIfBranchExists(releaseBranchName){
	println "PS I LOVE YOU:" +releaseBranchName
	bat 'git pull'
    def output = bat (script:"git ls-remote --heads origin "+releaseBranchName, returnStdout: true)
	println output
	respuesta= (!output?.trim())?false:true
	println respuesta
	return respuesta
}
def checkIfBranchUpdated(currentBranch,releaseBranchName){
	bat 'git pull'
	println("BEGINS  checkIfBranchUpdated(): "+ "git log origin/"+releaseBranchName+"..origin/"+currentBranch)
	def output = bat (script:"git log origin/"+releaseBranchName+"..origin/"+currentBranch, returnStdout: true)
	println("BRAND UPDATED: "+ output)
	respuesta= (!output?.trim())?false:true

	return respuesta ;
}
def deleteBranch(releaseBranchName){
	bat '''
	git pull
	git push origin --delete '''+ releaseBranchName
}
def createBranch(releaseBranchName,currentBranch){
bat 
'''
'git reset --hard HEAD'
'git pull
'git checkout ''' +currentBranch+ '''
'git checkout -b ''' +releaseBranchName+ '''
'git push origin ''' +releaseBranchName+ '''
'''


}
return this;