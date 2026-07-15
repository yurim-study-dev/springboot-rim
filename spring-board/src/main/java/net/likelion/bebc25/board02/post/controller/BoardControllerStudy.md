# BoardController 코드 설명

```java
// @Controller : 이 클래스가 스프링 MVC 컨트롤러임을 선언
// 브라우저 요청을 받아서 처리하고 화면(템플릿)을 반환하는 역할
@Controller

// @Slf4j : log.debug() 같은 로그 기능을 쓸 수 있게 해주는 어노테이션 (lombok)
@Slf4j

// @RequestMapping("/02/board") : 이 컨트롤러의 모든 URL 앞에 /02/board가 붙음
// 예) @GetMapping("/list.html") → 실제 URL: /02/board/list.html
@RequestMapping("/02/board")
public class BoardController {

    // DB 대신 임시로 사용하는 게시글 목록 (메모리에 저장)
    // final : 한 번 초기화하면 다시 바꿀 수 없음
    private final List<PostDto> fakePosts;

    // 생성자 : BoardController 객체가 만들어질 때 자동으로 실행됨
    // 여기서 샘플 게시글 2개를 미리 만들어 넣어둠
    public BoardController(){
        fakePosts = new ArrayList<PostDto>(); // 빈 리스트 생성

        PostDto post1 = new PostDto(); // 빈 객체 생성
        post1.setId(1);               // id 세팅
        post1.setTitle("1번 게시글");
        post1.setContent("1번 게시글 내용입니다.");
        post1.setAuthor("하루");
        post1.setCreatedAt(LocalDateTime.now()); // 현재 시간으로 세팅

        fakePosts.add(post1);
        fakePosts.add(post2);
    }

    // fakePosts 전체 목록을 반환하는 내부 메서드
    public List<PostDto> getPosts(){
        List<PostDto> list = fakePosts;
        return list;
    }

    // GET /02/board/list.html 요청 처리
    // 게시글 목록 전체를 가져와서 템플릿에 넘겨줌
    @GetMapping("/list.html")
    public String getBoardList(Model model){
        List<PostDto> posts = getPosts();

        // model : 템플릿(HTML)에 데이터를 전달하는 그릇
        // "posts" 라는 이름으로 list.html에서 ${posts}로 사용 가능
        model.addAttribute("posts", posts);
        return "board/list"; // templates/board/list.html 파일 반환
    }

    // GET /02/board/detail.html?id=1 요청 처리
    @GetMapping("/detail.html")
    public String getDetail(@RequestParam("id") int id, Model model){
        // @RequestParam("id") : URL의 ?id=값 을 int id 변수로 받음
        // get(id-1) : 리스트는 0부터 시작하므로 id=1이면 인덱스 0번
        PostDto post = getPosts().get(id-1);

        // "post" 라는 이름으로 detail.html에서 ${post.title} 등으로 사용 가능
        model.addAttribute("post", post);
        return "board/detail";
    }

    // GET /02/board/write.html 요청 처리 - 새 글 쓰기 화면
    @GetMapping("/write.html")
    public String getWriteForm(Model model){
        // new PostDto() : 빈 객체 → id가 0 (기본값)
        // 템플릿에서 id==0이면 "등록" 모드, id!=0이면 "수정" 모드로 구분
        model.addAttribute("postForm", new PostDto());
        return "board/write";
    }

    // GET /02/board/edit.html?id=1 요청 처리 - 수정 화면
    @GetMapping("/edit.html")
    public String getEditForm(@RequestParam("id") int id, Model model){
        PostDto post = getPosts().get(id-1); // 수정할 게시글 가져오기

        // write.html을 등록/수정 공용으로 사용
        // id가 0이 아니므로 템플릿에서 "수정" 모드로 동작
        model.addAttribute("postForm", post);
        return "board/write";
    }

    // POST /02/board/write 요청 처리 - 폼 데이터 받아서 등록
    @PostMapping("/write")
    public String writePost(@RequestParam("title") String title,
                            @RequestParam("content") String content,
                            @RequestParam("author") String author){
        // @RequestParam : 폼의 name 속성값으로 데이터를 받음
        // 예) <input name="title"> → String title로 받음

        PostDto post = new PostDto(title, content, author);
        log.debug(post.toString()); // 디버그 로그 출력 (개발 확인용)
        savePost(post);

        return "redirect:list.html"; // 등록 후 목록 페이지로 이동
    }

    // 게시글을 fakePosts 리스트에 저장하는 내부 메서드
    public void savePost(PostDto post){
        PostDto lastPost = getPosts().getLast(); // 마지막 게시글 가져오기
        post.setId(lastPost.getId() + 1);        // 마지막 id + 1 로 새 id 설정
        post.setCreatedAt(LocalDateTime.now());  // 등록 시간 설정
        fakePosts.add(post);                     // 리스트에 추가
    }

    // POST /02/board/edit 요청 처리 - 수정 폼 데이터 받아서 수정
    @PostMapping("/edit")
    public String editPost(@ModelAttribute PostDto post){
        // @ModelAttribute : 폼의 여러 필드를 PostDto 객체에 한 번에 담아줌
        // (id, title, author, content 등을 자동으로 매핑)
        log.debug(post.toString());
        updatePost(post);
        return "redirect:detail.html?id=" + post.getId(); // 수정 후 해당 글 상세로 이동
    }

    // 실제로 게시글 내용을 수정하는 내부 메서드
    public void updatePost(PostDto post){
        PostDto targetPost = null;
        for(PostDto org : getPosts()){
            if(org.getId() == post.getId()){ // id가 일치하는 게시글 찾음
                targetPost = org;
                break; // 찾았으면 루프 종료
            }
        }
        // 찾은 게시글의 내용을 새 내용으로 교체
        targetPost.setTitle(post.getTitle());
        targetPost.setContent(post.getContent());
        targetPost.setAuthor(post.getAuthor());
    }

    // fakePosts 리스트에서 게시글을 삭제하는 내부 메서드
    public void deletePost(int id){
        List<PostDto> posts = getPosts();
        for(PostDto org : posts){
            if(org.getId() == id){ // id가 일치하는 게시글 찾기
                posts.remove(org); // 리스트에서 제거
                break;
            }
        }
    }

    // POST /02/board/delete 요청 처리 (아직 미완성)
    @PostMapping("/delete")
    public String deletePost(){
        return "삭제 완료 후 보여줄 페이지";
    }
}
```
