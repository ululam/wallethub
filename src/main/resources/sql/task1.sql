ALTER TABLE votes ADD COLUMN rank INT NOT NULL;

SET @rank = 0;
UPDATE votes v SET v.rank = @rank := (@rank+1) ORDER BY v.votes DESC;