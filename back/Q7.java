

    // ***** Query 7 *****
    //
    // Find the name of the state with the most events, as well as the number of
    // events in that state.  If there is a tie, return the names of all of the (tied) states.
    //

    public void findEventStates() {
        this.eventCount = 12;
        this.popularStateNames.add("Michigan");
        this.popularStateNames.add("California");

        try (Statement stmt =
                     oracleConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                             ResultSet.CONCUR_READ_ONLY)) {

            // For each month, find the number of users born that month
            // Sort them in descending order of count
            ResultSet rst = stmt.executeQuery("select C.state_name,count(C.state_name) from tajik.public_cities C join tajik.public_user_events E on C.city_id=E.event_city_id group by C.state_name order by count(C.state_name) desc");
 
 			if(rst.next()){
 				this.eventCount = rst.getInt(2);
 				this.popularStateNames.add(rst.getString(1));
 			}

 			while(rst.next() && (this.eventCount == rst.getInt(2)){

 					this.popularStateNames.add(rst.getString(1));

 			}
            // Close statement and result set
            rst.close();
            stmt.close();
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }