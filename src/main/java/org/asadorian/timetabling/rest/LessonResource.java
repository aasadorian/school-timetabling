package org.asadorian.timetabling.rest;

import org.asadorian.timetabling.domain.Lesson;
import org.asadorian.timetabling.domain.Room;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/lessons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class LessonResource {

    @POST
    public Response add(Lesson lesson) {
        Lesson.persist(lesson);
        return Response.accepted(lesson).build();
    }

    @DELETE
    @Path("{lessonId}")
    public Response delete(@PathParam("lessonId") Long lessonId) {
        Lesson lesson = Lesson.findById(lessonId);
        if (lesson == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        lesson.delete();
        return Response.status(Response.Status.OK).build();
    }
}
