INSERT INTO version30.allocator (comments,contactemail,contactname ,created,username,password,isactive)
VALUES ('SYSTEM ALLOCATOR FOR AUSCOPE', 'services@ands.org.au', 'AUSCOPE', now(), 'AUSCOPE', '6367c48dd193d56ea7b0baad25b19455e529f5ee', true);
INSERT INTO version30.prefix (prefix,"version", description) VALUES ('XX', 0, 'ANDS TOP LEVEL PREFIX');
INSERT INTO version30.prefix (prefix,"version", description) VALUES ('XXAA', 0, 'AUSCOPE AAF USER PREFIX');
INSERT INTO version30.allocator_prefixes VALUES (1, 1);