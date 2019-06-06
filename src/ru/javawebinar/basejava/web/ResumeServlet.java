package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.storage.SqlStorage;
import ru.javawebinar.basejava.storage.Storage;
import ru.javawebinar.basejava.util.DateUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResumeServlet extends HttpServlet {
    private Storage storage;
    private TimePeriodOrganisation.TimePeriod emptyPeriod;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = new SqlStorage(Config.get().getDbUrl(), Config.get().getDbUser(), Config.get().getDbPassword());
        emptyPeriod = new TimePeriodOrganisation.TimePeriod(DateUtil.NOW, DateUtil.NOW, "", "");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume resume;
        try {
            resume = storage.get(uuid);
        } catch (NotExistStorageException e) {
            storage.save(resume = new Resume(uuid, fullName));
        }
        resume.setFullName(fullName);
        String addOrganisation = request.getParameter("addOrganisation");
        String addPeriod = request.getParameter("addPeriod");
        String deleteOrganisation = request.getParameter("deleteOrganisation");
        String deletePeriod = request.getParameter("deletePeriod");
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                resume.addContact(type, value);
            } else {
                resume.getContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        resume.addSection(type, new TextOnlySection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        resume.addSection(type, new TextListSection(value.split("\n")));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        String[] organisations = request.getParameterValues(type.name() + " organisationName");
                        String[] urls = request.getParameterValues(type.name() + " url");
                        List<TimePeriodOrganisation> organisationList = new ArrayList<>();
                        if (organisations != null) {
                            for (int i = 0; i < organisations.length; i++) {
                                if (deleteOrganisation != null
                                        && deleteOrganisation.equals(type.name() + " organisationID=" + i)) {
                                    continue;
                                }
                                String[] startDates = request.getParameterValues(type.name() + " organisationID=" + i + " startDate");
                                String[] endDates = request.getParameterValues(type.name() + " organisationID=" + i + " endDate");
                                String[] texts = request.getParameterValues(type.name() + " organisationID=" + i + " text");
                                String[] optionalTexts = request.getParameterValues(type.name() + " organisationID=" + i + " optionalText");
                                List<TimePeriodOrganisation.TimePeriod> periods = new ArrayList<>();
                                for (int j = 0; j < startDates.length; j++) {
                                    if (deletePeriod != null
                                            && deletePeriod.equals(type.name() + " organisationID=" + i + " periodID=" + j)
                                            && startDates.length > 1) {
                                        continue;
                                    }
                                    periods.add(new TimePeriodOrganisation.TimePeriod(DateUtil.parseDate(startDates[j]),
                                            DateUtil.parseDate(endDates[j]),
                                            texts[j],
                                            optionalTexts[j]));
                                }
                                if (addPeriod != null && addPeriod.equals(type.name() + " organisationID=" + i)) {
                                    periods.add(emptyPeriod);
                                }
                                organisationList.add(new TimePeriodOrganisation(new Contact(organisations[i], urls[i]), periods));
                            }
                        }
                        if (addOrganisation != null && addOrganisation.equals(type.name())) {
                            organisationList.add(new TimePeriodOrganisation("", "", emptyPeriod));
                        }
                        resume.addSection(type, new TimePeriodSection(organisationList));
                        break;
                    default:
                        break;
                }
            } else {
                resume.getSections().remove(type);
            }
        }
        storage.update(resume);
        response.sendRedirect(addOrganisation != null
                || addPeriod != null
                || deleteOrganisation != null
                || deletePeriod != null
                ? "resume?uuid=" + uuid + "&action=edit#orgs"
                : "resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume resume;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "create":
                response.sendRedirect("resume?uuid=" + UUID.randomUUID().toString() + "&action=edit");
                return;
            case "view":
            case "edit":
                try {
                    resume = storage.get(uuid);
                } catch (NotExistStorageException e) {
                    resume = new Resume(uuid, "New resume");
                }
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", resume);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }
}