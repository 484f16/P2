    public void suggestFriendsByMutualFriends(int n) {
        Long user1_id = 123L;
        String user1FirstName = "User1FirstName";
        String user1LastName = "User1LastName";
        Long user2_id = 456L;
        String user2FirstName = "User2FirstName";
        String user2LastName = "User2LastName";
        UsersPair p = new UsersPair(user1_id, user1FirstName, user1LastName, user2_id, user2FirstName, user2LastName);

        p.addSharedFriend(567L, "sharedFriend1FirstName", "sharedFriend1LastName");
        p.addSharedFriend(678L, "sharedFriend2FirstName", "sharedFriend2LastName");
        p.addSharedFriend(789L, "sharedFriend3FirstName", "sharedFriend3LastName");
        this.suggestedUsersPairs.add(p);

        

        try (Statement stmt =
                     oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                             ResultSet.CONCUR_READ_ONLY)) {

            // For each month, find the number of users born that month
            // Sort them in descending order of count
            ResultSet rst = stmt.executeQuery("create or replace view view_Q6 as select U1.user_id, U2.user_id, count(*) as count_pair,F1.user2_id as user3_id from tajik.public_users U1 join tajik.public_users U2 on U1.user_id<>U2.user_id join tajik.public_friends F1 on U1.user_id=F1.user1_id and U2.user_id<>F1.user2_id join tajik.public_friends F2 on U2.user_id=F2.user1_id and U1.user_id<>F2.user2_id and F1.user2_id=F2.user2_id group by U1.user_id,U2.user_id order by count(*) desc,U1.user_id asc,U2.user_id asc");

            while(rst.next()){

            }

            rst = stmt.executeQuery("select V.user1_id,U1.first_name,U2.last_name,V.user2_id,U2.first_name,U2.last_name,V.user3_id,U3.first_name,U3.last_name,V.count_pair from view_Q6 V, tajik.public_users U1, tajik.public_users U2, tajik.public_users U3 where V.user1_id=U1.user_id and V.user2_id=U2.user_id order by V.count_pair desc,U1.user_id asc,U2.user_id asc");

            if(rst.next() && n>0){
                p = new UsersPair(rst.getLong(1),rst.getString(2),rst.getString(3),rst.getLong(4),rst.getString(5),rst.getString(6));
                p.addSharedFriend(rst.getLong(7),rst.getString(8),rst.getString(9));
                n--;                
            }

            while (rst.next() && n>0){
                if(p.compareTo(UsersPair(rst.getLong(1),rst.getString(2),rst.getString(3),rst.getLong(4),rst.getString(5),rst.getString(6)))==0){
                    p.addSharedFriend(rst.getLong(7),rst.getString(8),rst.getString(9));
                }
                else{
                    this.suggestedUsersPairs.add(p);
                    p = new UsersPair(rst.getLong(1),rst.getString(2),rst.getString(3),rst.getLong(4),rst.getString(5),rst.getString(6))
                    p.addSharedFriend(rst.getLong(7),rst.getString(8),rst.getString(9));
                }
                n--;
            }

            // Close statement and result set
            rst.close();
            stmt.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }