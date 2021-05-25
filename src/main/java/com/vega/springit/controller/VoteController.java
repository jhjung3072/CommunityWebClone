package com.vega.springit.controller;

import com.vega.springit.domain.Link;
import com.vega.springit.domain.Vote;
import com.vega.springit.repository.LinkRepository;
import com.vega.springit.repository.VoteRepository;
import com.vega.springit.service.LinkService;
import com.vega.springit.service.VoteService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController //뷰를 생성하지 않을 것이기 때문에 Rest사용
public class VoteController {

    private VoteService voteService;
    private LinkService linkService;

    public VoteController(VoteService voteService, LinkService linkService) {
        this.voteService = voteService;
        this.linkService = linkService;
    }
    
    @Secured({"ROLE_USER"})
    @GetMapping("/vote/link/{linkID}/direction/{direction}/votecount/{voteCount}")
    public int vote(@PathVariable Long linkID, @PathVariable short direction, @PathVariable int voteCount){
        Optional<Link> optionalLink = linkService.findById(linkID);
        if(optionalLink.isPresent()){
            Link link=optionalLink.get();
            Vote vote= new Vote(direction, link);
            voteService.save(vote);

            int updateVoteCount=voteCount + direction;
            link.setVoteCount(updateVoteCount);
            linkService.save(link);
            return updateVoteCount;
        }
        return voteCount;
    }
}
