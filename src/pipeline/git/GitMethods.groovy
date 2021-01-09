package pipeline.git

def call(){
	
}
def checkIfBrachExists(releaseBranchName){
	def output = bat (script:"git ls-remote --heads origin "+releaseBranchName, returnStdout: true) 
	respuesta= (!output?.trim())?false:true
	return respuesta
}
def checkIfBrachUpdated(currentBranch,releaseBranchName){

	println("BEGINS  checkIfBrachUpdated(): "+ "git log origin/"+releaseBranchName+"..origin/"+currentBranch)
	def output = bat (script:"git log origin/"+releaseBranchName+"..origin/"+currentBranch, returnStdout: true)
	println("BRAND UPDATED: "+ output)
	respuesta= (!output?.trim())?false:true

	return respuesta ;
}
def deleteBranch(releaseBranchName){
bat "git push origin --delete " + releaseBranchName
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