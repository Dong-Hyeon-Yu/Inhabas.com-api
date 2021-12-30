package com.inhabas.api.repository.board;

import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.Board;
import com.inhabas.api.domain.member.Member;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Slf4j
public class MemoryBoardRepository implements BoardRepository {

    private final static ConcurrentHashMap<Integer, Board> store = new ConcurrentHashMap<>();
    private static final AtomicInteger sequence = new AtomicInteger(0);

    public MemoryBoardRepository() {
    }

    @Override
    public Board save(Board board) {
        Board boardToSave =
                new Board(sequence.incrementAndGet(), board.getTitle(), board.getContents(), board.getWriter(), board.getCategory());
        store.put(boardToSave.getId(), boardToSave);
        return boardToSave;
    }

    @Override
    public Optional<Board> findById(Integer id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Board> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Board> findAllByCategory(Category category) {
        if (category == null)
            return this.findAll();

        return store.values().stream()
                .filter(board -> board.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer id) {
        store.remove(id);
    }

    @Override
    public Board update(Board param) {
        Board findBoard = this.findById(param.getId())
                .orElseThrow(EntityNotFoundException::new);

        if (writerIsEquals(findBoard, param)) {
            updateBoard(findBoard, param);
        } else {
            // 예외 처리..
            return null;
        }
        return findBoard;
    }

    private void updateBoard(Board findBoard, Board param) {
        findBoard =
                new Board(findBoard.getId(), param.getTitle(), param.getContents(), findBoard.getWriter(), findBoard.getCategory());
    }

    private boolean writerIsEquals(Board findBoard, Board param) {
        // 게시판 작성자와 현재 수정을 시도하는 유저가 동일해야함.
        // 로그인 로직이 구현되지 않았으므로, 임시로 이렇게 해놓음.
        return Objects.equals(findBoard.getWriter(), param.getWriter());
    }

    public void clear() {
        store.clear();
    }

    @PostConstruct
    public void init() { // 테스트용 데이터
        Board board1 = new Board("게시글1", "아무내용", new Member(), Category.values()[1]);
        Board board2 = new Board("게시글2", "아무내용", new Member(), Category.values()[3]);

        this.save(board1);
        this.save(board2);
    }
}
