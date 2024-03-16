package seedu.address.logic.commands;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.*;
import seedu.address.model.tag.Tag;

import java.util.*;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.*;

public class SearchCommand extends Command {
    public static final String COMMAND_WORD = "search";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons where all their attributes match "
            + "the specified attributes.\n"
            + "For email, country, phone number and interviews only full words will be matched, while"
            + "name, comment and tags, partial words will be matched.\n"
            + "Parameters: [" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_COMMENT + "COMMENT] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_NO_FIELD_PROVIDED = "At least one field to search for must be provided.";

    private final SearchPersonDescriptor searchPersonDescriptor;

    public SearchCommand(SearchPersonDescriptor searchPersonDescriptor) {
        this.searchPersonDescriptor = searchPersonDescriptor;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(searchPersonDescriptor.getPredicate());
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof seedu.address.logic.commands.SearchCommand)) {
            return false;
        }

        seedu.address.logic.commands.SearchCommand otherSearchCommand
                = (seedu.address.logic.commands.SearchCommand) other;
        return searchPersonDescriptor.equals(otherSearchCommand.searchPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("searchPersonDescriptor", searchPersonDescriptor.toString())
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class SearchPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Comment comment;
        private Set<Tag> tags;

        public SearchPersonDescriptor() {}

//        /**
//         * Copy constructor.
//         * A defensive copy of {@code tags} is used internally.
//         */
//        public EditPersonDescriptor(SearchCommand.SearchPersonDescriptor toCopy) {
//            setName(toCopy.name);
//            setPhone(toCopy.phone);
//            setEmail(toCopy.email);
//            setAddress(toCopy.address);
//            setTags(toCopy.tags);
//        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldSpecified() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, comment, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setComment(Comment comment) {
            this.comment = comment;
        }

        public Optional<Comment> getComment() {
            return Optional.ofNullable(comment);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        public SearchPredicate getPredicate() {
            ArrayList<ContainsKeywordsPredicate> predicateList = new ArrayList<>();
            ContainsKeywordsPredicate<Name> nameSearch =
                    new ContainsKeywordsPredicate<>(PREFIX_NAME, this.getName());
            predicateList.add(nameSearch);
            ContainsKeywordsPredicate<Phone> phoneSearch =
                    new ContainsKeywordsPredicate<>(PREFIX_PHONE, this.getPhone());
            predicateList.add(phoneSearch);
            ContainsKeywordsPredicate<Email> emailSearch =
                    new ContainsKeywordsPredicate<>(PREFIX_NAME, this.getEmail());
            predicateList.add(emailSearch);
            ContainsKeywordsPredicate<Address> addressSearch =
                    new ContainsKeywordsPredicate<>(PREFIX_ADDRESS, this.getAddress());
            predicateList.add(addressSearch);
            ContainsKeywordsPredicate<Comment> commentSearch =
                    new ContainsKeywordsPredicate<>(PREFIX_COMMENT, this.getComment());
            predicateList.add(commentSearch);
            ContainsKeywordsPredicate<Set<Tag>> tagSearch =
                    new ContainsKeywordsPredicate<>(PREFIX_TAG, this.getTags());
            predicateList.add(tagSearch);
            return new SearchPredicate(predicateList);
//            if (this.getTags().isPresent()) {
//                for (Tag tag : this.getTags().get()) {
//                    predicateList.add(new ContainsKeywordsPredicate<>(PREFIX_TAG, Optional.of(tag)));
//                }
//            }
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof SearchCommand.SearchPersonDescriptor)) {
                return false;
            }

            SearchCommand.SearchPersonDescriptor otherSearchPersonDescriptor =
                    (SearchCommand.SearchPersonDescriptor) other;
            return Objects.equals(name, otherSearchPersonDescriptor.name)
                    && Objects.equals(phone, otherSearchPersonDescriptor.phone)
                    && Objects.equals(email, otherSearchPersonDescriptor.email)
                    && Objects.equals(address, otherSearchPersonDescriptor.address)
                    && Objects.equals(tags, otherSearchPersonDescriptor.tags);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("comment", comment)
                    .add("tags", tags)
                    .toString();
        }
    }
}
