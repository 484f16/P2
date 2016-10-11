    //@Override
    // ***** Query 8 *****
    // Given the ID of a user, find information about that
    // user's oldest friend and youngest friend
    //
    // If two users have exactly the same age, meaning that they were born
    // on the same day, then assume that the one with the larger user_id is older
    //
    public void findAgeInfo(Long user_id) {
        this.oldestFriend = new UserInfo(1L, "Oliver", "Oldham");
        this.youngestFriend = new UserInfo(25L, "Yolanda", "Young");
    }


        try (Statement stmt =
                     oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                             ResultSet.CONCUR_READ_ONLY)) {

            // For each month, find the number of users born that month
            // Sort them in descending order of count
            ResultSet rst = stmt.executeQuery("create view friendsOfUser as (select user2_id from tajik.punlic_friends F where F.user1_id="+'\''+user_id+'\''+" UNION select user1_id from tajik.public_friends F where F.user2_id="+'\''+user_id+"\')");
            while(rst.next()){

            }

            rst = stmt.executeQuery("select F.user_id,U.first_name,U.last_name from friendsOfUser F, tajik.public_users U where F.user_id=U.user_id order by U.year_of_birth asc, U.month_of_birth asc, U.day_of_birth asc, F.user_id desc");
            if(rst.next()){
                this.oldestFriend = new UserInfo(rst.getLong(1),rst.getString(2),rst.getString(3));
            }

            rst = stmt.executeQuery("select F.user_id,U.first_name,U.last_name from friendsOfUser F, tajik.public_users U where F.user_id=U.user_id order by U.year_of_birth desc, U.month_of_birth desc, U.day_of_birth desc, F.user_id asc");
            if(rst.next()){
                this.youngestFriend = new UserInfo(rst.getLong(1),rst.getString(2),rst.getString(3));
            }

                        // Close statement and result set
            rst.close();
            stmt.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }