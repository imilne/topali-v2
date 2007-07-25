#include "definitions.h"
#include "treeUtil.h"
#include "treeIt.h"
#include <fstream>
#include <iostream>
#include <cassert>
using namespace std;

vector<tree> getStartingTreeVecFromFile(string fileName) {
	vector<tree> vecT;
	ifstream inputf(fileName.c_str());
	if (inputf == NULL) {
		errorMsg::reportError("unable to open tree file");
	}
	while (!inputf.eof()) {
		//inputf.eatwhite();// do not remove. Tal: 1.1.2003
		vector<char> myTreeCharVec = PutTreeFileIntoVector(inputf);
		if (myTreeCharVec.size() >0) {
			tree t1(myTreeCharVec);
			//LOGDO(5,t1.output(myLog::LogFile()));
			vecT.push_back(t1);
		}
	}
	return vecT;
}

void getStartingTreeVecFromFile(string fileName,
												vector<tree>& vecT,
												vector<char>& constraintsOfT0) {
	ifstream inputf(fileName.c_str());
	if (inputf == NULL) {
		errorMsg::reportError("unable to open tree file");
	}
	//inputf.eatwhite();
	for (int i=0; !inputf.eof() ; ++i) {
//	while (!inputf.eof()) {
		vector<char> myTreeCharVec = PutTreeFileIntoVector(inputf);
		if (myTreeCharVec.size() >0) {
			if (i==0) {
				tree t1(myTreeCharVec,constraintsOfT0);
				vecT.push_back(t1);
			}
			else {
				tree t1(myTreeCharVec);
				vecT.push_back(t1);
			}

		}
	}
}







#include <algorithm>
using namespace std;

bool sameTreeTolopogy(tree t1, tree t2){
	if (t1.getNodesNum() != t2.getNodesNum()) {
		errorMsg::reportError("error in function same tree topology (1)");
	}
	tree::nodeP x = t2.getRoot();
	while (x->getNumberOfSons() > 0) x= x->getSon(0);
	t1.rootAt(t1.findNodeByName(x->name())->father()); // now they have the same root
	t2.rootAt(t2.findNodeByName(x->name())->father()); // now they have the same root
	vector<string> names1(t1.getNodesNum());
	treeIterDownTopConst tit1(t1);
	for (tree::nodeP nodeM = tit1.first(); nodeM != tit1.end(); nodeM = tit1.next()) {
		vector<string> nameOfChild;
		for (int i=0; i < nodeM->getNumberOfSons();++i) {
			nameOfChild.push_back(names1[nodeM->getSon(i)->id()]);
		}
		if (nodeM->getNumberOfSons()==0) nameOfChild.push_back(nodeM->name());
		sort(nameOfChild.begin(),nameOfChild.end());
		string res = "(";
		for (int k=0; k < nameOfChild.size(); ++k) {
			res += nameOfChild[k];
		}
		res += ")";
		names1[nodeM->id()] = res;
	}

	vector<string> names2(t1.getNodesNum());
	treeIterDownTopConst tit2(t2);
	for (tree::nodeP nodeM2 = tit2.first(); nodeM2 != tit2.end(); nodeM2 = tit2.next()) {
		vector<string> nameOfChild;
		for (int i=0; i < nodeM2->getNumberOfSons();++i) {
			nameOfChild.push_back(names2[nodeM2->getSon(i)->id()]);
		}
		if (nodeM2->getNumberOfSons()==0) nameOfChild.push_back(nodeM2->name());
		sort(nameOfChild.begin(),nameOfChild.end());
		string res = "(";
		for (int k=0; k < nameOfChild.size(); ++k) {
			res += nameOfChild[k];
		}
		res += ")";
		names2[nodeM2->id()] = res;
	}
	return names1[t1.getRoot()->id()] == names2[t2.getRoot()->id()];
	


}


void cutTreeToTwo(tree bigTree,
				  const string& nameOfNodeToCut,
				  tree &small1,
				  tree &small2){// cutting above the NodeToCut.
	// we want to cut the tree in two.
	// first step: we make a new node between the two nodes that have to be splited,
	tree::nodeP node2splitOnNewTree = bigTree.findNodeByName(nameOfNodeToCut);
	string interNode = "interNode";
	assert(node2splitOnNewTree->father() != NULL);
	tree::nodeP tmp = makeNodeBetweenTwoNodes(bigTree,node2splitOnNewTree->father(),node2splitOnNewTree, interNode);
	bigTree.rootAt(tmp);
	cutTreeToTwoSpecial(bigTree,tmp, small1,small2);
	tree::nodeP toDel1 = small1.findNodeByName(interNode);
	small1.removeLeaf(toDel1);
	tree::nodeP toDel2 = small2.findNodeByName(interNode);
	small2.removeLeaf(toDel2);
	// this part fix the ids.
	treeIterTopDown tIt(small1);
	int newId =0;
	for (tree::nodeP mynode = tIt.first(); mynode != tIt.end(); mynode = tIt.next()) {
		mynode->setID(newId);
		newId++;
	}
	treeIterTopDown tIt2(small2);
	int newId2 =0;
	for (tree::nodeP mynode2 = tIt2.first(); mynode2 != tIt2.end(); mynode2 = tIt2.next()) {
		mynode2->setID(newId2);
		newId2++;
	}

};

// pre-request:
// the intermediateNode is the root.
// and it has two sons.
// resultT1PTR & resultT2PTR are empty trees (root=NULL);
void cutTreeToTwoSpecial(const tree& source, tree::nodeP intermediateNode,
						 tree &resultT1PTR, tree &resultT2PTR) {
	// make sure that you got two empty trees:
	if (resultT1PTR.getRoot() != NULL) 
		errorMsg::reportError("got a non empty tree1 in function cutTreeToTwoSpecial"); 
	else if (resultT2PTR.getRoot() != NULL) 
		errorMsg::reportError("got a non empty tree2 in function cutTreeToTwoSpecial"); 

	// make sure the the intermediateNode is really an intermediate Node;
	if ((intermediateNode->getNumberOfSons() !=2 ) || (source.getRoot() != intermediateNode)) {
		errorMsg::reportError("intermediateNode in function cutTreeToTwoSpecial, is not a real intermediate node ");
	}

	resultT1PTR.createRootNode();
	resultT1PTR.getRoot()->setName(intermediateNode->name());

	resultT2PTR.createRootNode();
	resultT2PTR.getRoot()->setName(intermediateNode->name());

	
	resultT1PTR.recursiveBuildTree(resultT1PTR.getRoot(),intermediateNode->getSon(0));
	resultT1PTR.recursiveBuildTree(resultT2PTR.getRoot(),intermediateNode->getSon(1));
}





//insert a new node between fatherNode and sonNode
tree::nodeP makeNodeBetweenTwoNodes(tree& et,
											tree::nodeP fatherNode,
											tree::nodeP sonNode,
											const string &interName){
	//make sure that fatherNode is indeed the father and sonNode is the son (and not the opposite).
	if (fatherNode->father() == sonNode) {
		tree::nodeP tmp = fatherNode;
		fatherNode = sonNode;
		sonNode = tmp;
	}
	else if (sonNode->father() != fatherNode) {
		errorMsg::reportError("Error in function 'cut_tree_in_two'. the two nodes are not neighbours ");
	}	
	
	tree::nodeP theNewNodePTR = new tree::TreeNode(et.getNodesNum());

	//fix the tree information for the new node.
	theNewNodePTR->setName(interName);
	MDOUBLE tmpLen = sonNode->dis2father() * 0.5;
	theNewNodePTR->setDisToFather(tmpLen);
	theNewNodePTR->setFather(fatherNode);
	theNewNodePTR->setSon(sonNode);

	//fix the tree information for the father node.
	fatherNode->removeSon(sonNode);
	fatherNode->setSon(theNewNodePTR);

	//fix the tree information for the sonNode.
	sonNode->setFather(theNewNodePTR);
	sonNode->setDisToFather(tmpLen);
	return theNewNodePTR;
}

vector<string> getSequencesNames(const tree& t){
	vector<tree::nodeP> vleaves;
	t.getAllLeaves(vleaves,t.getRoot());
	vector<string> res;
	vector<tree::nodeP>::const_iterator i = vleaves.begin();
	for ( ; i<vleaves.end(); ++i) {
		res.push_back((*i)->name());
	}
	return res;
}

tree starTree(const vector<string>& names) {
	tree et;
	et.createRootNode();
	for (int k=0 ; k < names.size(); ++k) {
		tree::nodeP tmpNode;
		tmpNode = et.createNode(et.getRoot(),et.getNodesNum());
		tmpNode->setDisToFather(tree::FLAT_LENGTH_VALUE);
		tmpNode->setName(names[k]);
	}
	et.create_names_to_internal_nodes();
	return et;
}