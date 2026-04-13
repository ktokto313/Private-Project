INSERT INTO Departments (name) VALUES
('Human Resources'), ('Finance'), ('IT Support'), ('Software Engineering'),
('Quality Assurance'), ('DevOps'), ('Customer Support'), ('Sales'),
('Marketing'), ('Legal'), ('Procurement'), ('Operations'),
('Logistics'), ('R&D'), ('Product Management'), ('Data Science'),
('Security'), ('Compliance'), ('Training'), ('Administration'),
('Business Intelligence'), ('Infrastructure'), ('Network Operations'),
('Cloud Engineering'), ('Technical Support'), ('Field Services'),
('Customer Success'), ('UX/UI Design'), ('Content Team'),
('Partnerships'), ('Public Relations'), ('Strategy'),
('Risk Management'), ('Audit'), ('Accounting'), ('Payroll'),
('Recruitment'), ('Learning & Development'), ('IT Helpdesk'),
('Database Administration'), ('Mobile Development'), ('Web Development'),
('AI Engineering'), ('Automation'), ('Support Tier 2'),
('Support Tier 3'), ('Vendor Management'), ('Facilities'),
('Internal Tools'), ('Platform Engineering');

INSERT INTO Users (role, username, password, departmentID)
SELECT
    (ARRAY['USER','IT','ADMIN'])[1 + (i % 3)]::role,
    lower(first_name || '.' || last_name || i),
    '$2b$10$examplehashedpasswordstring1234567890abcd', -- bcrypt-like
    (i % 50) + 1
FROM (
    SELECT i,
           (ARRAY['An','Binh','Chi','Duc','Huy','Khanh','Linh','Minh','Nam','Phuong',
                  'Quang','Trang','Tuan','Vy','Yen','Long','Hai','Son','Thao','Hoa'])[1 + (i % 20)] AS first_name,
           (ARRAY['Nguyen','Tran','Le','Pham','Hoang','Vu','Phan','Dang','Bui','Do'])[1 + (i % 10)] AS last_name
    FROM generate_series(1, 50) s(i)
) t;

INSERT INTO Priorities (levelOfPriority, name, timeToRespond, timeToFinish)
VALUES
(1, 'Critical', '5 minutes', '1 hour'),
(2, 'High', '15 minutes', '4 hours'),
(3, 'Medium', '1 hour', '1 day'),
(4, 'Low', '4 hours', '3 days'),
(5, 'Very Low', '1 day', '7 days');

INSERT INTO TicketTypes (title, description) VALUES
('Bug', 'Software defect or unexpected behavior'),
('Feature Request', 'Request for new functionality'),
('Incident', 'System outage or critical issue'),
('Service Request', 'General support request'),
('Access Request', 'Permission or account related'),
('Performance Issue', 'Slow or degraded system'),
('Security Issue', 'Potential vulnerability or breach'),
('Data Issue', 'Incorrect or missing data'),
('UI/UX Issue', 'Interface or usability problem'),
('Integration Issue', 'Third-party or API problem');

INSERT INTO Tickets (
    title, detail, creator, state, priority, ticketType,
    timeCreated, timeProcessing, timeResolved, assignee, cause
)
SELECT
    t.title,
    t.detail,
    (i % 50) + 1,
    (ARRAY['CREATED','PROCESSING','RESOLVED','DONE'])[1 + (i % 4)]::state,
    (i % 5) + 1,
    (i % 10) + 1,
    NOW() - (i || ' hours')::interval,
    NOW() - ((i - 1) || ' hours')::interval,
    NOW() - ((i - 2) || ' hours')::interval,
    ((i + 5) % 50) + 1,
    t.cause
FROM (
    SELECT i,
        (ARRAY[
            'Cannot login to system',
            'Page loads very slowly',
            'Error when submitting form',
            'Request access to dashboard',
            'Application crashes on startup',
            'Data not syncing correctly',
            'Email notifications not sent',
            'UI layout broken on mobile',
            'API returns 500 error',
            'Unable to reset password'
        ])[1 + (i % 10)] AS title,

        (ARRAY[
            'User reports invalid credentials error despite correct password.',
            'Dashboard takes more than 10 seconds to load.',
            'Form submission throws unknown server error.',
            'User needs access to analytics dashboard.',
            'App crashes immediately after launch.',
            'Records are not updating across services.',
            'Users are not receiving system emails.',
            'Layout overlaps on smaller screens.',
            'Backend service failing intermittently.',
            'Password reset link not working.'
        ])[1 + (i % 10)] AS detail,

        (ARRAY[
            'Authentication service misconfiguration',
            'Database performance bottleneck',
            'Unhandled exception in backend',
            'Missing role assignment',
            'Client-side bug',
            'Synchronization job failure',
            'SMTP server issue',
            'CSS rendering bug',
            'API gateway timeout',
            'Token expiration misconfigured'
        ])[1 + (i % 10)] AS cause
    FROM generate_series(1, 50) s(i)
) t;

INSERT INTO Comments (detail, creator, ticketID, timeCreated)
SELECT
    (ARRAY[
        'Looking into this issue.',
        'Can you provide more details?',
        'Issue reproduced successfully.',
        'Fix deployed to staging.',
        'Awaiting user confirmation.',
        'Resolved, please verify.',
        'Escalating to senior team.',
        'Logs indicate possible root cause.',
        'Temporary workaround applied.',
        'Closing ticket due to inactivity.'
    ])[1 + (i % 10)],
    (i % 50) + 1,
    (i % 50) + 1,
    NOW() - (i || ' minutes')::interval
FROM generate_series(1, 50) s(i);

INSERT INTO Attachments (
    content, attachedObjectType, contentType, attachedObjectID
)
VALUES
(decode(md5('log1'), 'hex'), 'TICKET', 'text/plain', 1),
(decode(md5('screenshot1'), 'hex'), 'TICKET', 'image/png', 2),
(decode(md5('error_dump'), 'hex'), 'TICKET', 'application/json', 3),
(decode(md5('trace'), 'hex'), 'COMMENT', 'text/plain', 4),
(decode(md5('ui_bug'), 'hex'), 'TICKET', 'image/png', 5),
(decode(md5('report'), 'hex'), 'COMMENT', 'application/pdf', 6),
(decode(md5('api_log'), 'hex'), 'TICKET', 'text/plain', 7),
(decode(md5('config'), 'hex'), 'COMMENT', 'application/xml', 8),
(decode(md5('dump'), 'hex'), 'TICKET', 'application/octet-stream', 9),
(decode(md5('note'), 'hex'), 'COMMENT', 'text/plain', 10);