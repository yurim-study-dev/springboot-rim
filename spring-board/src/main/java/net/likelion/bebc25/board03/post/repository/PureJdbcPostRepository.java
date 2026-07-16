package net.likelion.bebc25.board03.post.repository;

import net.likelion.bebc25.board03.post.dto.PostDto;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PureJdbcPostRepository implements PostRepository {

    private  String url = "jdbc:mysql://localhost:3306/board_db?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private  String user = "user1";
    private  String password = "1111";

    @Override
    public List<PostDto> findAll() {

        String sql = "SELECT id, title, author,secret, created_at AS createdAt FROM post2";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        List<PostDto> result = new ArrayList<>();

        try{ // 플랜 A
            // 1. 데이터베이스 연결(Connection 객체 생성)
            conn = DriverManager.getConnection(url, user, password);

            // 2. SQL 실행 객체 생성(Statement 객체 생성)
            stmt = conn.createStatement();

            // 3. SQL 실행
            rs = stmt.executeQuery(sql);

            // 4. 결과 처리(ResultSet 사용)
            while(rs.next()){
                PostDto postDto = new PostDto();
                postDto.setId(rs.getInt("id"));
                postDto.setTitle(rs.getString("title"));
                postDto.setAuthor(rs.getString("author"));
                postDto.setCreatedAt(rs.getObject("createdAt", LocalDateTime.class));
                postDto.setSecret(rs.getBoolean("secret"));

                result.add(postDto);


            }

        }catch(Exception e){ // 플랜 B
            System.out.println("에러 발생: " + e.getMessage());
            e.printStackTrace();
        }finally{
            // 5. 생성된 리소스를 생성의 역순으로 해제
            try{ if(rs != null) rs.close(); } catch (Exception e){ }
            try{ if(stmt != null) stmt.close(); } catch (Exception e){ }
            try{ if(conn != null) conn.close(); } catch (Exception e){ }
        }
        return result;
    }

    @Override
    public PostDto findById(int id) {
        return null;
    }

    @Override
    public void save(PostDto post) {

    }

    @Override
    public void update(PostDto post) {

    }

    @Override
    public void deleteById(int id) {

    }
}
