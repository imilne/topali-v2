#ifndef ___LIKELIHOOD_COMPUTATION
#define ___LIKELIHOOD_COMPUTATION

#include "definitions.h"
#include "computePijComponent.h"
#include "sequenceContainer.h"
#include "suffStatComponent.h"

namespace likelihoodComputation {
	MDOUBLE getLofPos(const int pos,					// this function is used
					  const tree& et,					// when the br-len
					  const sequenceContainer& sc,		// are the same for all
					  const computePijHom& pi,			// positions.
					  const stochasticProcess& sp);

// --------------------------------------------------------------------------------
// this function should be used only when the branch lengths are not the same for
// all positions. Otherwise, computePijHom should be calculated once,
// and be used for all calls. In this function, computePijHom is being computed for
// each position.
MDOUBLE getLofPosHomModelEachSiteDifferentRate(const int pos,			
					  const tree& et,					
					  const sequenceContainer& sc,		
					  const stochasticProcess& sp);
// ---------------------------------------------------------------------------------


// --------------------------------------------------------------------------------
// this function should be used only when the branch lengths are not the same for
// all positions. Otherwise, computePijHom should be calculated once,
// and be used for all calls. In this function, computePijHom is being computed for
// each position.
MDOUBLE getLofPosGamModelEachSiteDifferentRate(const int pos,
					  const tree& et,
					  const sequenceContainer& sc,
					  const stochasticProcess& sp);
// --------------------------------------------------------------------------------


MDOUBLE getLofPos(const int pos,					// this function is used
					  const tree& et,					// when gamma, and the br-len
					  const sequenceContainer& sc,		// are the same for all pos.
					  const computePijGam& pi,
					  const stochasticProcess& sp);
	MDOUBLE getLofPos(const int pos,					// with a site specific rate.
					  const tree& et,
					  const sequenceContainer& sc,
					  const stochasticProcess& sp,
					  const MDOUBLE gRate);
	MDOUBLE getProbOfPosWhenUpIsFilledHom(const int pos,	// to be used for homogenous model
					  const tree& et,
					  const sequenceContainer& sc,
					  const stochasticProcess& sp,
					  const suffStatGlobalHomPos& ssc);
	MDOUBLE getProbOfPosWhenUpIsFilledGam(const int pos, // to be used for Gamma model.
						const tree& et,
						const sequenceContainer& sc,
						const stochasticProcess& sp,
						const suffStatGlobalGamPos& cup);

	MDOUBLE getTreeLikelihoodFromUp(const tree& et,
									const sequenceContainer& sc,
									const stochasticProcess& sp,
									const suffStatGlobalGam& cup,
									const Vdouble * weights =0 );
	MDOUBLE getTreeLikelihoodFromUp2(const tree& et,
						const sequenceContainer& sc,
						const stochasticProcess& sp,
						const suffStatGlobalGam& cup,
						Vdouble& posLike, // fill this vector with each position likelihood but without the weights.
						const Vdouble * weights=0);
	MDOUBLE getTreeLikelihoodAllPosAlphTheSame(const tree& et,
							const sequenceContainer& sc,
							const stochasticProcess& sp,
										const Vdouble * const weights=0);
	// fill this vector with each position posterior rate (p(r|D))
	// but without the weights.
	// the weights are used only because we return the likelihood 
	// (this takes these weights into account).
	MDOUBLE getPosteriorOfRates(const tree& et,
						const sequenceContainer& sc,
						const stochasticProcess& sp,
						const suffStatGlobalGam& cup,
						VVdouble& posteriorLike, 
						const Vdouble * weights);

	MDOUBLE getPosteriorOfRates(const tree& et,
						const sequenceContainer& sc,
						const stochasticProcess& sp,
						VVdouble& posteriorLike, 
						const Vdouble * weights);

	// From Itay M.
	// this function forces non gamma computation of likelihoods from up.
	// i.e., even if the stochastic process is really gamma - the likelihood is computed as if there's no gamma.
	MDOUBLE getTreeLikelihoodFromUpSpecifcRates(const tree& et,
						const sequenceContainer& sc,
						const stochasticProcess& sp,
						const suffStatGlobalHom& cup,
						Vdouble& posLike, // fill this vector with each position likelihood but without the weights.
						const Vdouble * weights);

};



#endif

