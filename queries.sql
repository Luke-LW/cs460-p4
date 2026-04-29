-- Query 1
-- For a given User, list all their Bookmarked messages across all conversations, including the conversation title and the timestamp

-- Select all bookmarks and join with message and conversations to get neccessary details. 
SELECT 
    b.uid,
    u.username, 
    c.title, 
    m.message, 
    m.timestamp
FROM Bookmark b
JOIN Message m ON b.mid = m.mid          
JOIN Conversation c ON m.cid = c.cid
JOIN User u ON b.uid = u.uid
-- Only show bookmarks where the userID matches the "given user" (replace ? with the actual user ID when executing the query with prepared statement)
WHERE b.uid = ?;




-- Query 2
-- List all users who have “Unpaid” invoices, including their email, the total amount owed, and the date of their last conversation

-- Select from Users, joing with BillingRecord and Invoice to find neccessary information.
SELECT 
    u.uid, 
    u.email, 
    SUM(i.amount) AS total_amount_owed, 
    MAX(msg.last_conversation_date) AS last_conversation_date
FROM User u
JOIN BillingRecord br ON u.uid = br.uid
JOIN Invoice i ON br.brid = i.brid                     
LEFT JOIN (
    -- Subquery to find the most recent message (last conversation activity) per user
    -- Only select the message from this user with the latest timestamp, which is also the date of their last conversation
    SELECT c.uid, MAX(m.timestamp) AS last_conversation_date
    FROM Message m
    JOIN Conversation c ON m.cid = c.cid
    GROUP BY c.uid
) msg ON u.uid = msg.uid
-- Only consider invoices where the status is 'Unpaid'
WHERE i.status = 'Unpaid'
GROUP BY u.uid, u.email
-- Verify the total amount is greater than 0 an the user's unpaid invoice actually has money due
HAVING SUM(i.amount) > 0;





-- Query 3
-- Identify the “Most Helpful” Persona: List the persona name that has received the highest percentage of “Thumbs Up” feedback across all conversations linked to it.

WITH PersonaStats AS (
    -- Subquery to find the total number of AI messages and the count of positive and negative ratings for each persona
    -- Select Persona's where the message that received feedback is an AI message, and the rating is either 1 (thumbs up) or -1 (thumbs down)
    SELECT 
        p.pid, 
        p.name AS persona_name, 
        COUNT(*) AS total_ai_messages, 
        SUM(CASE WHEN m.rating = 1 THEN 1 ELSE 0 END) AS positive_ratings, 
        SUM(CASE WHEN m.rating = -1 THEN 1 ELSE 0 END) AS negative_ratings
    FROM Persona p
    JOIN Conversation c ON p.pid = c.pid
    JOIN Message m      ON c.cid = m.cid
    WHERE m.sender = 'AI'                           -- Only keep AI-generated messages
      AND m.rating IS NOT NULL 
      AND m.rating IN (1, -1)                    
    GROUP BY p.pid, p.name
)
-- Select the persona name that has received the highest percent of thumbs up feedback
SELECT 
    persona_name,  
    thumbs_up_count, 
    total_feedback, 
    ROUND(100.0 * thumbs_up_count / NULLIF(total_feedback, 0), 2) AS thumbs_up_percentage
FROM PersonaStats
ORDER BY thumbs_up_percentage DESC, total_feedback DESC
FETCH FIRST 1 ROW ONLY; -- Only keep the "most helpful" persona with the highest percentage of thumbs up feedback







-- Query 4
-- Design your own query
-- What users have sent the most messages and what is their average message length?

-- Select users, their membership tier, the total number of messages they have sent, the average message length in characters, the date of their last message, and the number of conversations they have participated in. 
SELECT 
    u.username, 
    mbr.tier AS membership_tier, 
    COUNT(*) AS total_messages_sent, 
    ROUND(AVG(LENGTH(m.message)), 2) AS avg_message_length_chars, 
    MAX(m.timestamp) AS last_message_date, 
    COUNT(DISTINCT c.cid) AS num_conversations
FROM User u
JOIN Membership mbr ON u.mid = mbr.mid                    -- Get membership tier
JOIN Conversation c ON u.uid = c.uid                      -- Conversations created by the user
JOIN Message m      ON c.cid = m.cid                      -- Messages in those conversations
WHERE m.sender = 'User'                                   -- Only count messages sent by users 
GROUP BY u.username, mbr.tier
ORDER BY total_messages_sent DESC, avg_message_length_chars DESC -- Sort by total messages sent first, then by average message length to break ties
FETCH FIRST 5 ROWS ONLY; -- Only keep the top 5 most active users