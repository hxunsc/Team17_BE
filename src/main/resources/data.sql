insert into team_tag (tag_attribute, tag_name, is_deprecated)
values ('성별', '여성', false),
       ('성별', '남성', false),
       ('성별', '무관', false),
       ('나이', '10대', true),
       ('나이', '20대', false),
       ('나이', '30대', false),
       ('나이', '40대', false),
       ('나이', '50대', false),
       ('나이', '전연령', false),
       ('운동강도', '강', false),
       ('운동강도', '중', false),
       ('운동강도', '약', false),
       ('운동강도', '자유', false);

insert into member (email, nickname, exercise_attendance_date, created_at, is_deprecated)
values ('test1@naver.com', 'test1', '0', '2024-10-04T12:30:00', false),
       ('test2@naver.com', 'test2', '0', '2024-10-04T12:40:00', false),
       ('test3@naver.com', 'test3', '0', '2024-10-04T12:40:00', false);

insert into team (team_name, team_description, leader_id, max_participants, current_participants,
                  password, is_deprecated)
values ('test1 team', 'test1 team description', 1, 8, 2, null, false),
       ('test2 team', 'test2 team description', 1, 8, 1, '1234', false),
       ('test3 team', 'test3 team description', 3, 8, 2, '1234', false),
       ('test4 team', 'test4 team description', 3, 8, 1, '1234', false);


insert into team_member_mapping (member_id, team_id, is_deprecated)
values (1, 1, false),
       (2, 1, false),
       (1, 3, false);

insert into chatting (team_member_mapping_id, message, created_at, is_deprecated)
values (1, '채팅 테스트 1', '2024-10-04T12:30:00', false),
       (1, '채팅 테스트 2', '2024-10-04T12:30:00', false);

insert into team_tag_mapping (team_tag_id, team_id, is_deprecated)
values (1, 1, false),
       (4, 1, false),
       (2, 2, false),
       (5, 2, false),
       (9, 3, false),
       (1, 3, false),
       (2, 3, false),
       (5, 4, false),
       (8, 4, false);

insert into exercise (exercise_name, is_deprecated, member_id)
values ('test1 User exercise1', false, 1),
       ('test1 User exercise2', false, 1),
       ('test2 User exercise1', false, 2),
       ('test2 User exercise2', false, 2);

insert into exercise_time (start_time, exercise_time, is_active, exercise_id, is_deprecated)
values ('2024-10-10 10:10:00', 6600, false, 1, false),
       ('2024-10-10 10:10:00', 5400, false, 3, false);

insert into exercise_history (exercise_id, created_at, exercise_history_time, is_deprecated)
values (1, '2024-10-09 03:00:10', 5400, false),
       (2, '2024-10-08 03:00:20', 3600, false),
       (3, '2024-10-09 03:00:10', 3600, false),
       (4, '2024-10-08 03:00:20', 5400, false);

insert into product (image_url, product_url, name, price, store_name, view_count, is_deprecated)
values ('https://tinyurl.com/25ntsq9k', 'https://tinyurl.com/27sk7wrj', '런닝머신', 240000, 'coupang', 15, false),
       ('https://tinyurl.com/2ylb52aq', 'https://tinyurl.com/2cxw4zxa', '실내자전거', 190000, 'coupang', 5, false),
       ('https://tinyurl.com/28y6jl8d', 'https://tinyurl.com/29m67lsf', '폼롤러', 5000, 'adidas', 15, false),
       ('https://tinyurl.com/2954eoum', 'https://brand.naver.com/adidasfitness/products/10824318636', '요가매트', 24000, 'adidas', 8, false),
       ('https://tinyurl.com/2cpy86bd', 'https://tinyurl.com/2y9fzpw8', '짐볼', 9900, 'body crew', 20, false),
       ('https://tinyurl.com/23oej7er', 'https://tinyurl.com/2bkxjcyd', '요가 밴드', 7900, 'coupang', 11, false),
       ('https://tinyurl.com/28g7dayl', 'https://tinyurl.com/26czhyqs', '아몬드 브리즈 언스위트', 15550, 'coupang', 25, false);

insert into product_tag (tag_name, is_deprecated)
values ('운동 상품', false),
       ('운동 식품', false);

insert into product_tag_mapping (product_id, product_tag_id, is_deprecated)
values (1, 1, false),
       (2, 1, false),
       (3, 1, false),
       (4, 1, false),
       (5, 1, false),
       (6, 1, false),
       (7, 2, false);



