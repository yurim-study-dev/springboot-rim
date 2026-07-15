package net.likelion.bebc25.board02.post.controller;

import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.board02.post.dto.PostDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/02/board")
public class BoardController {

    private final List<PostDto> fakePosts;

    public BoardController(){
        fakePosts = new ArrayList<PostDto>();

        PostDto post1 = new PostDto();
        post1.setId(1);
        post1.setTitle("1번 게시글");
        post1.setContent("1번 게시글 내용입니다.");
        post1.setAuthor("하루");
        post1.setSecret(true);
        post1.setCreatedAt(LocalDateTime.now());

        PostDto post2 = new PostDto();
        post2.setId(2);
        post2.setTitle("2번 게시글");
        post2.setContent("2번 게시글 내용입니다.");
        post2.setAuthor("나무");
        post2.setCreatedAt(LocalDateTime.now());

        fakePosts.add(post1);
        fakePosts.add(post2);


    }

    // 모든 게시글 목록을 반환한다.
    public List<PostDto> getPosts(){
        List<PostDto> list = fakePosts;
        return list;
    }

    // 게시글 목록 조회하는 컨트롤러
    @GetMapping("/list.html")
    public String getBoardList(Model model){
        // 게시글 목록 조회(데이터)
        List<PostDto> posts = getPosts();

        model.addAttribute("posts", posts);
        return "board/list";
    }

    // 게시글 상세 조회하는 컨트롤러
    @GetMapping("/detail.html")
    public String getDetail(@RequestParam("id") int id, Model model){
        PostDto post = getPost(id);

        model.addAttribute("post", post);
        return "board/detail"; // 템플릿 파일 경로
    }

    // 지정한 id의 게시글을 반환한다.
    public PostDto getPost(int id){
        PostDto targetPost = null;
        List<PostDto> posts = getPosts();
        for(PostDto org : posts){
            if(org.getId() == id){
                targetPost = org;
                break;
            }
        }
        return targetPost;
    }

    // 게시글 등록 화면을 요청하는 컨트롤러
    @GetMapping("/write.html")
    public String getWriteForm(@ModelAttribute("postForm") PostDto post){ // 모델에 자동으로 주입까지 됨(postDto 이름으로)
//        model.addAttribute("postForm", new PostDto());
        return "board/write";
    }

    // 게시글 수정 화면을 요청하는 컨트롤러
    @GetMapping("/edit.html")
    public String getEditForm(@RequestParam("id") int id, Model model){
        PostDto post = getPost(id);

        model.addAttribute("postForm", post);
        return "board/write";
    }

    // 게시글 등록 요청을 처리하는 컨트롤러
    @PostMapping("/write")
    public String writePost(@ModelAttribute PostDto post){
        log.debug(post.toString());

        savePost(post);

        return "redirect:list.html"; // 브라우저에 list.html로 재요청하라고 응답
    }

    // 게시글을 등록한다.
    public void savePost(PostDto post){
        PostDto lastPost = getPosts().getLast();
        post.setId(lastPost.getId() + 1);
        post.setCreatedAt(LocalDateTime.now());
        fakePosts.add(post);
    }

    // 게시글 수정 요청을 처리하는 컨트롤러
    @PostMapping("/edit")
    public String editPost(@ModelAttribute PostDto post){
        log.debug(post.toString());
        updatePost(post);
        return "redirect:detail.html?id=" + post.getId();
    }

    // 게시글을 수정한다.
    public void updatePost(PostDto post){
        PostDto targetPost = null;
        List<PostDto> posts = getPosts();
        for(PostDto org : posts){
            if(org.getId() == post.getId()){
                targetPost = org;
                break;
            }
        }

//        posts.remove(targetPost);

        targetPost.setTitle(post.getTitle());
        targetPost.setContent(post.getContent());
        targetPost.setAuthor(post.getAuthor());
    }

    // 게시글을 삭제한다.
    public void removePost(int id){
        List<PostDto> posts = getPosts();
        for(PostDto org : posts){
            if(org.getId() == id){
                posts.remove(org);
                break;
            }
        }
    }

    // 게시글 삭제 요청을 처리하는 컨트롤러
    @PostMapping("/delete")
    public String deletePost(@RequestParam int id){
        log.debug(id + " 삭제 요청.");
        removePost(id);
        return "redirect:list.html";
    }

}