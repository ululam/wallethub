set @date_start = '1970-01-01';
set @date_end = '2015-12-31';


select count(*) from bugs
	where @date_start < @date_end
		AND open_date < close_date
        AND open_date <= @date_end
        AND close_date > @date_start;